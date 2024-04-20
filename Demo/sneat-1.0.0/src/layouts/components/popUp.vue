<template>
  <div>
    <button @click="showPopup = !showPopup">
      <VIcon
        class="me-2"
        icon="bx-plus"
        size="22"
      />
    </button>
    <div v-if="showPopup" class="popup">
      <div class="popup-content">
        <!-- Content for your pop-up goes here -->
        <h2>This is a Pop-up</h2>
        <p>Click the button to close.</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showPopup: false,
    };
  },
};
</script>

<style>
/* Style for the pop-up */
.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999; /* Adjust the z-index as needed to overlay other content */
}

.popup-content {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
</style>
