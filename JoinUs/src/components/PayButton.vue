<template>
  <div>
    <button class="checkout-btn" @click="handlePayClick">
      Pay
    </button>
    <TicketForm v-if="showForm" :visible="showForm" @close="showForm = false" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TicketForm from './TicketForm.vue'

const showForm = ref(false)

let user = null
const tickets = ref([])

onMounted(() => {
  const userData = localStorage.getItem('user')
  const ticketsData = localStorage.getItem('tickets')
  if (userData) {
    try {
      user = JSON.parse(userData)
    } catch (err) {
      console.error('Invalid user data in localStorage')
    }
  }
  if (ticketsData) {
    try {
      tickets.value = JSON.parse(ticketsData)
    } catch (err) {
      console.error('Invalid tickets data in localStorage')
    }
  }
})

const handlePayClick = async () => {
  if (user) {
    try {
      const ticketIds = tickets.value.map(ticket => ticket.id)
      console.log("ticketIds :", ticketIds);
      const response = await fetch('http://backend:8080/api/commandes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          registeredUser: true,
          userId: user.id,
          ticketIds: ticketIds
        })
      })

      if (!response.ok) {
        throw new Error('Error when booking ticket')
      }
      localStorage.removeItem('tickets')
      window.location.href = `/myorders?userId=${user.id}`

    } catch (err) {
      console.error('Error when booking ticket:', err)
      alert('An error occurred while booking. Please try again.')
    }
  } else {
    showForm.value = true
  }
}
</script>

<style scoped>

</style>
