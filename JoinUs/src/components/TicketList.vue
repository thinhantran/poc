<template>
  <div>
    <div v-if="tickets.length === 0">
      <p>You don't have any tickets yet.</p>
    </div>
    <div v-else>
      <div v-for="(ticket, index) in tickets" :key="index" class="ticket" @click="goToEvent(ticket.eventTitle)">
        <img class="ticket-img" :src="ticket.image" :alt="ticket.eventTitle" />
        <div class="ticket-details">
          <div class="event-title">{{ ticket.eventTitle }}</div>
          <div class="event-meta">{{ ticket.date }} at {{ formatTime(ticket.time) }}</div>
        </div>
        <div class="ticket-info">
          <div class="quantity-control">
            <button @click.stop="updateQuantity(index, -1)">−</button>
            <span>{{ ticket.quantity }}</span>
            <button @click.stop="updateQuantity(index, 1)">+</button>
          </div>
          <div class="price">{{ ticket.total?.toFixed(2) }}€</div>
        </div>
        <button class="delete-btn" @click.stop="deleteTicket(index)">🗑️</button>
      </div>

      <div class="total-text">
        Total: <strong>{{ totalAmount.toFixed(2) }}€</strong>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { API_BASE } from '../utils/api.ts';

const tickets = ref([])
const totalAmount = ref(0)

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user'))

  if (user) {
    fetch(`${API_BASE}/api/tickets/user/${user.id}`)
        .then(res => res.json())
        .then(data => {
          tickets.value = data
          localStorage.setItem('tickets', JSON.stringify(data))
          totalAmount.value = data.reduce((sum, t) => sum + t.total, 0)
        })
        .catch(err => {
          console.error('Error loading tickets:', err)
        })
  } else {
    const local = JSON.parse(localStorage.getItem('tickets') || '[]')
    tickets.value = local
    totalAmount.value = local.reduce((sum, t) => sum + t.total, 0)
  }
})

const formatTime = (timeStr) => {
  if (!timeStr) return '';
  const is12hFormat = /am|pm/i.test(timeStr);
  let date;
  if (is12hFormat) {
    date = new Date(`January 1, 1970 ${timeStr}`);
  } else {
    date = new Date(`1970-01-01T${timeStr}`);
  }
  if (isNaN(date.getTime())) {
    console.warn('Invalid time string:', timeStr);
    return timeStr;
  }
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

const updateQuantity = (index, delta) => {
  const ticket = tickets.value[index]
  const pricePerItem = ticket.total / ticket.quantity
  ticket.quantity += delta

  if (ticket.quantity <= 0) {
    tickets.value.splice(index, 1)
  } else {
    ticket.total = pricePerItem * ticket.quantity
  }

  localStorage.setItem('tickets', JSON.stringify(tickets.value))
  totalAmount.value = tickets.value.reduce((sum, t) => sum + t.total, 0)

  const user = JSON.parse(localStorage.getItem('user'))
  if (user && ticket.id) {
    fetch(`${API_BASE}/api/tickets`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: ticket.id, quantity: ticket.quantity })
    }).catch(err => console.error('Error updating ticket:', err))
  }
}

const deleteTicket = (index) => {
  const ticket = tickets.value[index]
  if (!ticket) return

  tickets.value.splice(index, 1)
  localStorage.setItem('tickets', JSON.stringify(tickets.value))
  totalAmount.value = tickets.value.reduce((sum, t) => sum + t.total, 0)

  const user = JSON.parse(localStorage.getItem('user'))
  if (user && ticket.id) {
    fetch(`${API_BASE}/api/tickets?ticketId=${ticket.id}`, {
      method: 'DELETE',
    })
        .then(res => {
          if (!res.ok) throw new Error('Failed to delete ticket on server')
        })
        .catch(err => {
          console.error('Error deleting ticket from server:', err)
        })
  }
}
const slugify = (str) => {
  return str.toLowerCase().replace(/\s+/g, '-').replace(/[^\w-]/g, '');
};
const goToEvent = (eventTitle) => {
  if (eventTitle) {
    const slug = slugify(eventTitle);
    window.location.href = `/events/${slug}`;
  }
};
</script>

<style scoped>
.ticket {
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 10px;
  display: flex;
  gap: 1rem;
  align-items: center;
  background-color: #fff4e6;
  border: 1px solid #f1c40f33;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.ticket-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
}

.ticket-details {
  flex: 1;
}

.event-title {
  font-weight: 600;
  font-size: 1.1rem;
}

.event-meta {
  font-size: 0.9rem;
  color: #666;
}

.quantity-control button {
  margin: 0 5px;
  background-color: #d35400;
  color: white;
  border: none;
  padding: 0.3rem 0.6rem;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

.quantity-control span {
  font-weight: bold;
  margin: 0 0.5rem;
}

.price {
  font-weight: bold;
  font-size: 1.1rem;
  color: #d35400;
}

.delete-btn {
  background: transparent;
  border: none;
  font-size: 1.3rem;
  cursor: pointer;
  color: #e74c3c;
  top: -1px;
}

.total-text {
  margin-top: 1.5rem;
  text-align: right;
  font-size: 1.2rem;
  font-weight: bold;
  color: #d35400;
}
</style>
