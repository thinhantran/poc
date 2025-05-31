<template>
  <div class="action-buttons">
    <a href="/about" class="btn">About Project</a>
    <a href="/mytickets" class="btn">My tickets</a>
    <div v-if="user" class="user-menu" style="position:relative; display:inline-block;">
      <span class="btn" @click="toggleDropdown" style="cursor:pointer;">
        {{ user.fullName || user.email || 'User' }}
      </span>
      <div v-show="dropdownVisible" class="dropdown">
        <a v-if="user && user.id" :href="`/myorders?userId=${user.id}`" class="dropdown-link">My Orders</a>
        <button @click="logout" class="dropdown-link">Log Out</button>
      </div>
    </div>
    <template v-else>
      <a href="/signin?mode=signup" class="btn">Sign Up</a>
      <a href="/signin?mode=login" class="btn">Log In</a>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const user = ref(null);
const dropdownVisible = ref(false);

onMounted(() => {
  const stored = localStorage.getItem('user');
  if (stored) {
    user.value = JSON.parse(stored);
    console.log("user :", user.value);
  }
});

function logout() {
  localStorage.removeItem('user');
  localStorage.removeItem('tickets');
  user.value = null;
  dropdownVisible.value = false;
  window.location.href = '/';
  //window.location.reload();
}

function toggleDropdown() {
  dropdownVisible.value = !dropdownVisible.value;
}
</script>

<style scoped>
.action-buttons {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn {
  padding: 0.5rem 1.2rem;
  background-color: #e67e22;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  text-decoration: none;
  transition: background-color 0.2s ease, transform 0.1s ease;
  cursor: pointer;
}

.btn:hover {
  background-color: #cf711f;
  transform: translateY(-1px);
}

.btn:active {
  transform: translateY(0);
}

a.btn {
  text-decoration: none;
}

.dropdown {
  position: absolute;
  right: 0;
  top: 100%;
  background: white;
  border: 1px solid #ddd;
  padding: 0.5rem 0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-width: 180px;
  z-index: 1000;
}

.dropdown-link {
  display: block;
  width: 100%;
  padding: 0.6rem 1rem;
  font-size: 0.95rem;
  font-weight: 500;
  color: #333;
  text-align: left;
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease;
  box-sizing: border-box;
}


.dropdown-link:hover {
  background-color: #fcefe1;
  color: #e67e22;
}


</style>
