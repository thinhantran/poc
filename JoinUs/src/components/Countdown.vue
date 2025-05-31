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
  console.log("date :",eventDateISO);

  if (props.eventDate.includes('/')) {
    eventDateISO = convertToISO(props.eventDate);
  }

  console.log("date ISO :",eventDateISO);

  let eventTime = new Date(eventDateISO);
  if (isNaN(eventTime.getTime())) {
    const today = new Date();
    const [h, m] = props.eventDate.split(':').map(Number);
    eventTime = new Date(today.getFullYear(), today.getMonth(), today.getDate(), h, m, 0);
  }
  console.log("eventTime :",eventTime);

  const diff = eventTime - now;
  console.log("diff :",diff);


  if (diff <= 0) {
    countdownDisplay.value = 'The event is happening now or already passed!';
    clearInterval(intervalId);
    return;
  }

  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
  const minutes = Math.floor((diff / (1000 * 60)) % 60);
  const seconds = Math.floor((diff / 1000) % 60);

  console.log("days :",days);

  if (days >= 1) {
    countdownDisplay.value = `${days}d ${hours}h ${minutes}m`;
    console.log("date recul if :",countdownDisplay.value);
  } else {
    countdownDisplay.value = `${padZero(hours)}:${padZero(minutes)}:${padZero(seconds)}`;
    console.log("date recul else :",countdownDisplay.value);

  }

};

function convertToISO(dateStr) {
  const [datePart, timePartWithMeridian] = dateStr.split('T');
  const [month, day, year] = datePart.split('/').map(Number);
  const [timePart, meridian] = timePartWithMeridian.split(' ');
  let [hour, minute] = timePart.split(':').map(Number);

  // Chuyển AM/PM sang 24h format
  if (meridian === 'PM' && hour !== 12) hour += 12;
  if (meridian === 'AM' && hour === 12) hour = 0;

  // Tạo chuỗi ISO chuẩn
  const iso = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:00`;
  return iso;
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
