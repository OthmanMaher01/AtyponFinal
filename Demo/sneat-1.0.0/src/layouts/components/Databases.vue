<script setup>
import axios from "axios";

const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})
const createDatabase = async (databaseName) => {
  try {
    const url = `http://localhost:8080/database/create?databaseName=${encodeURIComponent(databaseName)}`;
    const response = await axios.get(url);

  } catch (error) {
    console.error(error);
  }
};

const showPopup = ref(false);
const userInput = ref('');

const submitUserInput = () => {
  // Access the user input in userInput.value
  const userEnteredData = userInput.value;
  console.log('User input:', userEnteredData);
  createDatabase(userEnteredData)

  // Close the popup
  showPopup.value = false;
};

const closePopUp = () =>{
  showPopup.value = false;
}

</script>

<template>
  <li
    class="nav-link"
    :class="{ disabled: item.disable }"
  >
    <Component
      :is="item.to ? 'RouterLink' : 'a'"
      :to="item.to"
      :href="item.href"
    >
      <!-- 👉 Title -->
      <span class="nav-item-title">
        {{ item.title }}
      </span>
      <span>      </span>
      <VIcon
        :icon="item.icon"
        class="nav-item-icon"
      />

      <VListItem>
        <template #prepend>

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

                <button @click="closePopUp"><VIcon
                  class="me-2"
                  icon="bx-x"
                  size="22"
                /></button>
                <!-- Content for your pop-up goes here -->
                <h2>Create Database</h2>
                <div class="input-container">
                  <input v-model="userInput" placeholder="Database Name" class="input-field"/>
                  <div class="submit-button">
                    <button @click="submitUserInput" style="text-align: center">Submit</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>

      </VListItem>
    </Component>
  </li>
</template>

<style lang="scss">
.layout-vertical-nav {
  .nav-link a {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
}

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

.input-container {
  margin-top: 10px; /* Add some spacing between the input and button */
}

.input-field {
  width: 100%; /* Make the input field take up the full width */
  padding: 8px; /* Add padding to the input field */
  border: 1px solid #ccc; /* Add a border */
  border-radius: 4px; /* Add rounded corners */
  font-size: 16px; /* Adjust the font size */

}

.submit-button {
  margin-top: 10px; /* Add spacing between the input and button */
  //padding: 8px 16px; /* Add padding to the button */
  background-color: rgb(var(--v-theme-primary)) !important; /* Change the button's background color */
  color: rgb(var(--v-theme-on-primary)) !important; /* Change the button's text color to white */
  border: none; /* Remove the button's border */
  border-radius: 4px; /* Add rounded corners to the button */
  cursor: pointer; /* Change the cursor to a pointer on hover */
  //font-size: 16px; /* Adjust the font size */
  --v-btn-size: 0.875rem;
  --v-btn-height: 38px;
  font-size: 0.875rem;
  min-width: 68px;
  padding: 0px 16px;
}

.submit-button:hover {
  background-color: #0056b3; /* Change the button's background color on hover */
}
</style>
