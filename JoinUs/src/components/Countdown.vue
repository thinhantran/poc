<template>
  <div class="countdown">
    <h3>⏳ Time left to event:</h3>
    <p>{{ countdownDisplay }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  eventDate: String,
})

const countdownDisplay = ref('')
let intervalId = null

function padZero(num) {
  return num.toString().padStart(2, '0')
}

const updateCountdown = () => {
  const now = new Date();
  let eventDateISO = props.eventDate;

  if (props.eventDate.includes('/')) {
    eventDateISO = convertToISO(props.eventDate);
  }

  let eventTime = new Date(eventDateISO);
  if (isNaN(eventTime.getTime())) {
    const today = new Date();
    const [h, m] = props.eventDate.split(':').map(Number);
    eventTime = new Date(today.getFullYear(), today.getMonth(), today.getDate(), h, m, 0);
  }

  const diff = eventTime - now;

  if (diff <= 0) {
    countdownDisplay.value = 'The event is happening now or already passed!';
    clearInterval(intervalId);
    return;
  }

  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
  const minutes = Math.floor((diff / (1000 * 60)) % 60);
  const seconds = Math.floor((diff / 1000) % 60);

  if (days >= 1) {
    countdownDisplay.value = `${days}d ${hours}h ${minutes}m`;
  } else {
    countdownDisplay.value = `${padZero(hours)}:${padZero(minutes)}:${padZero(seconds)}`;
  }
};

function convertToISO(dateStr) {
  const [datePart, timePart] = dateStr.split('T');
  const [day, month, year] = datePart.split('/');
  return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}T${timePart}`;
}



onMounted(() => {
  updateCountdown()
  intervalId = setInterval(updateCountdown, 1000)
})

onUnmounted(() => {
  clearInterval(intervalId)
})
</script>

<style scoped>
.countdown {
  background: #fef6e4;
  border: 1px solid #e67e22;
  padding: 1rem;
  border-radius: 10px;
  margin: 1.5rem 0;
  text-align: center;
  font-weight: 500;
  color: #333;
}
</style>
