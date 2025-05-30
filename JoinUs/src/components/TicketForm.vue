<template>
  <div class="modal-overlay" v-show="visible" @click="closeModal">
    <form class="modal" @click.stop @submit.prevent="submitForm">
      <button type="button" class="modal-close" @click="closeModal" aria-label="Close modal">×</button>
      <h2 class="modal-title">Book Your Ticket</h2>

      <div class="form-group">
        <label for="name">Full Name</label>
        <input v-model="form.name" type="text" id="name" placeholder="Your full name" required />
      </div>

      <div class="form-group">
        <label for="email">Email Address</label>
        <input v-model="form.email" type="email" id="email" placeholder="you@example.com" required />
      </div>

      <div class="form-group">
        <label for="phone">Phone Number</label>
        <input v-model="form.phone" type="tel" id="phone" placeholder="+123 456 7890" required />
      </div>

      <button v-if="!showPDF" type="submit" class="submit-btn">Confirm Booking</button>
      <button v-if="showPDF" class="submit-btn" @click="downloadPDF">
        📄 Download Ticket PDF
      </button>
    </form>

  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['close'])

const form = ref({
  name: '',
  email: '',
  phone: ''
})

const showPDF = ref(false)
const pdfUrl = ref('')
const commandeId = ref(null)


const closeModal = () => {
  emit('close')
}

const submitForm = async () => {
  try {
    const ticketIds = await createGuestTickets()

    const response = await fetch('http://backend:8080/api/commandes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        registeredUser: false,
        fullName: form.value.name,
        email: form.value.email,
        phone: form.value.phone,
        ticketIds: ticketIds
      })
    })

    if (!response.ok) {
      throw new Error('Error when booking ticket')
    }
    commandeId.value = response.headers.get('X-Commande-Id')
    const blob = await response.blob()
    pdfUrl.value = window.URL.createObjectURL(blob)
    showPDF.value = true
  } catch (err) {
    console.error('Booking failed:', err)
    alert('Booking failed. Please try again later.')
  }
}

const downloadPDF = async () => {
  if (!commandeId.value) return

  try {
    console.log("commandeId :", commandeId.value);
    const response = await fetch(`http://backend:8080/api/commandes/download/${commandeId.value}`, {
      method: 'GET',
      credentials: 'include'
    })

    if (!response.ok) throw new Error('Unable to load ticket')

    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `ticket-${commandeId.value}.pdf`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (err) {
    console.error('Error loading ticket:', err)
    alert('Unable to load ticket. Please try again.')
  }
}


const createGuestTickets = async () => {
  const ticketsData = localStorage.getItem('tickets')
  let parsedTickets = []

  if (ticketsData) {
    try {
      parsedTickets = JSON.parse(ticketsData)
    } catch (err) {
      console.error('Invalid tickets data')
      return []
    }
  }

  const payload = {
    tickets: parsedTickets.map(t => ({
    eventTitle: t.eventTitle,
    date: t.date,
    time: t.time,
    quantity: t.quantity
  }))}

  const res = await fetch('http://localhost:8080/api/tickets/guest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
    credentials: 'include'
  })

  if (!res.ok) throw new Error('Failed to create guest tickets')
  const ticketIds = await res.json()
  return ticketIds
}


watch(() => props.visible, (newVal) => {
  if (newVal) {
    form.value = {
      name: '',
      email: '',
      phone: '',
      tickets: 1
    }
  }
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  transition: opacity 0.3s ease;
}

.modal {
  background: white;
  border-radius: 10px;
  max-width: 420px;
  width: 90%;
  padding: 2rem 2.5rem;
  box-shadow: 0 10px 30px rgba(0,0,0,0.15);
  position: relative;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.modal-close {
  position: absolute;
  top: 15px;
  right: 15px;
  background: transparent;
  border: none;
  font-size: 1.8rem;
  cursor: pointer;
  color: #888;
  transition: color 0.2s ease;
}
.modal-close:hover {
  color: #d35400;
}

.modal-title {
  margin-bottom: 1.5rem;
  font-weight: 700;
  font-size: 1.5rem;
  color: #333;
  text-align: center;
}

.form-group {
  margin-bottom: 1.25rem;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.4rem;
  font-weight: 600;
  color: #555;
  font-size: 0.95rem;
}

input[type="text"],
input[type="email"],
input[type="tel"],
input[type="number"] {
  padding: 0.6rem 0.8rem;
  border: 1.8px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
  outline-offset: 2px;
}

input[type="text"]:focus,
input[type="email"]:focus,
input[type="tel"]:focus,
input[type="number"]:focus {
  border-color: #d35400;
  outline: none;
  box-shadow: 0 0 8px rgba(211, 84, 0, 0.3);
}

.submit-btn {
  background-color: #d35400;
  color: white;
  font-weight: 700;
  font-size: 1.1rem;
  padding: 0.75rem;
  width: 100%;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 0.5rem;
  box-shadow: 0 4px 8px rgba(211, 84, 0, 0.4);
}

.submit-btn:hover {
  background-color: #e67e22;
  box-shadow: 0 6px 12px rgba(230, 126, 34, 0.6);
}

@media (max-width: 480px) {
  .modal {
    padding: 1.5rem 1.8rem;
  }
}
</style>
