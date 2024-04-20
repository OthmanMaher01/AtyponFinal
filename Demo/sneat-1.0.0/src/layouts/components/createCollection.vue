<!--<script setup>
import axios from "axios";

const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})
const createCollection = async (databaseName, collectionName) => {
  try {
    const url = `http://localhost:8080/collection/create?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&`;
    const response = await axios.get(url);

  } catch (error) {
    console.error(error);
  }
};
const showPopup = ref(false);
const userInput = ref(''); // Initialize user input

const submitUserInput = (databaseName) => {
  // Access the user input in userInput.value
  const userEnteredData = userInput.value;
  console.log('User input:', userEnteredData);

  createCollection( databaseName, userEnteredData)

  // Close the popup
  showPopup.value = false;
};

</script>-->
<script setup>
import axios from "axios"
import { ref, reactive } from 'vue';

import { defineProps } from 'vue';

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const jsonInput = ref(''); // Initialize JSON input
const showPopup = ref(false);
const userInput = ref(''); // Initialize user input
const collections = reactive({
  list: [],
});
const createCollection = async (databaseName, collectionName, schema) => {
  try {
    const url = `http://localhost:8080/collection/create?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}`;

    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    const response = await axios.post(url, schema, { headers });
    console.log('Response Content-Type:', response.headers['content-type']);

    // Handle the response as needed
    console.log('Response:', response.data);
    collections.list.push(response.data); // Assuming the response contains the new collection data
  } catch (error) {
    console.error('Error:', error);
  }
};

// const submitUserInput = () => {
//   // Access the user input in userInput.value
//   const userEnteredData = userInput.value;
//   console.log('User input:', userEnteredData);
//
//   createCollection(userEnteredData)
//
//   // Close the popup
//   showPopup.value = false;
// };

const submitUserInput = () => {
  // Access the user input in userInput.value (collection name)
  const collectionName = userInput.value;
  console.log('Collection Name:', collectionName);

  // Access the JSON input in jsonInput.value (schema)
  const schema = jsonInput.value;
  console.log('Schema (JSON Input):', schema);

  const databaseName = props.item.parent;
  console.log(databaseName)

  // Call your createCollection method with all parameters
  createCollection(databaseName, collectionName, schema);

  // Close the popup
  showPopup.value = false;

  // Clear the input fields after submission
  userInput.value = '';
  jsonInput.value = '';
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
      <VListItem>
        <template #prepend>
          <div>
            <button @click="showPopup = !showPopup">
              {{ item.title }}
              <VIcon
                class="me-2"
                icon="bx-plus"
                size="22"
              />
            </button>

            <div v-if="showPopup" class="popup">
              <div class="popup-content">
                <button @click="closePopUp">
                  <VIcon
                    class="me-2"
                    icon="bx-x"
                    size="22"
                  />
                </button>
                <!-- Content for your pop-up goes here -->
                <h2>Create Collection</h2>
                <div class="input-container">
                  <input v-model="userInput" placeholder="Collection Name" class="input-field"/>
                </div>
                <div class="input-container">
                  <textarea v-model="jsonInput" placeholder="Schema Data" class="input-field" rows="4"></textarea>
                </div>
                <div class="submit-button">
                  <button @click="submitUserInput">Submit</button>
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
  padding: 8px 16px; /* Add padding to the button */
  background-color: #007BFF; /* Change the button's background color */
  color: #fff; /* Change the button's text color to white */
  border: none; /* Remove the button's border */
  border-radius: 4px; /* Add rounded corners to the button */
  cursor: pointer; /* Change the cursor to a pointer on hover */
  font-size: 16px; /* Adjust the font size */

}

.submit-button:hover {
  background-color: #0056b3; /* Change the button's background color on hover */
}
</style>
