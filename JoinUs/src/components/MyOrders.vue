<template>
  <div class="my-orders">
    <h1>My Orders</h1>

    <div v-if="orders.length === 0" class="no-orders">
      You have no orders yet.
    </div>
    <div v-else class="orders-list">
      <div v-for="order in orders" class="order-card">
        <div class="order-header">
          <button @click="downloadPDF(order.commandeId)" class="pdf-button">
            Download pdf
          </button>
          <h2>Order #{{ order.createdAt }}</h2>
        </div>
        <div class="tickets">
          <div v-for="ticket in order.tickets" :key="ticket.id" class="ticket">
            <img :src="ticket.image" :alt="ticket.eventTitle" class="ticket-image" />
            <div class="ticket-info">
              <h3>{{ ticket.eventTitle }}</h3>
              <p><strong>Date:</strong> {{ ticket.date }}</p>
              <p><strong>Time:</strong> {{ ticket.time }}</p>
              <p><strong>Quantity:</strong> {{ ticket.quantity }}</p>
              <p><strong>Price per ticket:</strong> {{ ticket.unitPrice.toFixed(2) }} €</p>
              <p><strong>Total:</strong> {{ ticket.total.toFixed(2) }} €</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  orders: {
    type: Array,
    required: true,
  },
  userId: {
    type: [String, Number],
    required: true,
  },
});

function downloadPDF(commandeId) {
  const url = `http://backend:8080/api/commandes/download/${commandeId}`;
  window.open(url, '_blank');
}
</script>

<style scoped>
.my-orders {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1rem;
  font-family: Arial, sans-serif;
}

.no-orders {
  text-align: center;
  font-size: 1.2rem;
  color: #666;
  margin-top: 2rem;
}

.order-card {
  border: 1px solid #e67e22;
  border-radius: 10px;
  padding: 1rem;
  margin-bottom: 2rem;
  background-color: #fff3e0;
  box-shadow: 0 2px 8px rgb(230 126 34 / 0.2);
}

.order-card h2 {
  color: #d35400;
  margin-bottom: 1rem;
}

.tickets {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.ticket {
  display: flex;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgb(0 0 0 / 0.1);
  width: 100%;
  max-width: 400px;
  overflow: hidden;
}

.ticket-image {
  width: 120px;
  height: 90px;
  object-fit: cover;
}

.ticket-info {
  padding: 0.5rem 1rem;
  flex: 1;
}

.ticket-info h3 {
  margin: 0 0 0.5rem;
  color: #d35400;
}

.ticket-info p {
  margin: 0.15rem 0;
  font-size: 0.9rem;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
}

.pdf-button {
  background-color: #e67e22;
  border: none;
  color: white;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s ease;
}

.pdf-button:hover {
  background-color: #d35400;
}
</style>
