<template>
  <div class="countdown">
    <h3>⏳ Time left to event:</h3>
    <p v-if="diff<= 0">{{ countdownDisplay }}</p>
    <div v-else class="countdown-container">
      <div v-if="days>=1" class="countdown-pad">
        <span class="number">{{days}}</span>
        <span v-if="days>1" class="day-unit">Days</span>
        <span v-else class="day-unit">Day</span>

      </div>
      <div class="countdown-pad">
        <span class="number">{{hours}}</span>
        <span class="day-unit">Hours</span>

      </div>
      <div class="countdown-pad">
        <span class="number">{{minutes}}</span>
        <span class="day-unit">Minutes</span>
      </div>
    </div>
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

const days = ref(0);
const hours = ref(0);
const minutes = ref(0);

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

  days.value = Math.floor(diff / (1000 * 60 * 60 * 24));
  hours.value = Math.floor((diff / (1000 * 60 * 60)) % 24);
  minutes.value = Math.floor((diff / (1000 * 60)) % 60);
  const seconds = Math.floor((diff / 1000) % 60);


  if (days >= 1) {
    countdownDisplay.value = `${days}d ${hours}h ${minutes}m`;
  } else {
    countdownDisplay.value = `${padZero(hours)}:${padZero(minutes)}:${padZero(seconds)}`;

  }

};

function convertToISO(dateStr) {
  const [datePart, timePartWithMeridian] = dateStr.split('T');
  const [month, day, year] = datePart.split('/').map(Number);
  const [timePart, meridian] = timePartWithMeridian.split(' ');
  let [hour, minute] = timePart.split(':').map(Number);

  if (meridian === 'PM' && hour !== 12) hour += 12;
  if (meridian === 'AM' && hour === 12) hour = 0;

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

.countdown-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 400px;
  justify-self: center;
}

.countdown-pad {

  display: flex;
  flex-direction: column;
  border-radius: 10px;
  width: 100px;
  background: antiquewhite;
  padding: 5px 0;
  box-shadow: 0 0 2px coral;
}
.number {
  color: coral;
  font-size: 2rem;
}

.day-unit {
  color: gray;
  font-weight: bolder;
  font-size: .8rem;
}
</style>
