<template>
  <form @submit.prevent="handleSearch" class="search-form">
    <div class="search-container">
      <input
          v-model="query"
          type="text"
          class="search-input"
          placeholder="Search events..."
      />
      <button type="submit" class="search-btn">
        🔍
      </button>
    </div>
  </form>
</template>

<script setup>
import { ref } from 'vue';

const query = ref('');

const handleSearch = () => {
  if (query.value.trim()) {
    window.location.href = `/search?name=${encodeURIComponent(query.value)}`;
  }
};
</script>

<style scoped>
.search-form {
  display: flex;
  justify-content: center;
  margin-top: 1rem;
}

.search-container {
  display: flex;
  align-items: center;
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
}

.search-input {
  border: none;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  outline: none;
  width: 250px;
}

.search-btn {
  background-color: #d35400;
  border: none;
  color: white;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 1.2rem;
}
</style>
