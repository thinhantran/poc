<template>
  <button class="ticket-button" @click="buyTicket">
    {{ price ? `Buy Ticket (${price}€)` : 'Get Free Ticket' }}
  </button>
</template>

<script setup>
const props = defineProps({
  price: Number,
  title: String,
  date: String,
  time: String,
  image: String,
  id:Number,
})

const buyTicket = async () => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user && user.id) {
    console.log("id event :",props.id, "id user :", user.id);
    try {
      const response = await fetch('http://backend:8080/api/tickets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          userId: user.id,
          eventId: props.id
        })
      });

      if (!response.ok) {
        throw new Error('Unable to create ticket from server');
      }
      window.location.href = '/mytickets';

    } catch (error) {
      console.error('Lỗi khi gọi API:', error);
      alert('Có lỗi xảy ra. Vui lòng thử lại sau.');
    }

  } else {
    const newTicket = {
      eventTitle: props.title,
      date: props.date,
      time: props.time || '00:00',
      quantity: 1,
      total: props.price || 0,
      image: props.image,
    }
    addTicketToLocalStorage(newTicket);
    window.location.href = '/mytickets'
  }

}

function addTicketToLocalStorage(newTicket) {
  const tickets = JSON.parse(localStorage.getItem('tickets') || '[]');

  const existingTicket = tickets.find(ticket =>
      ticket.title === newTicket.title &&
      ticket.date === newTicket.date &&
      ticket.time === newTicket.time
  );

  if (existingTicket) {
    existingTicket.quantity += 1;
    existingTicket.total = newTicket.price * existingTicket.quantity;
  } else {
    tickets.push(newTicket);
  }
  localStorage.setItem('tickets', JSON.stringify(tickets));
}

</script>


