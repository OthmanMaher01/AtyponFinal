<script setup>
import axios from "axios";
import { onMounted, ref, watch } from "vue";
import { useRoute } from 'vue-router';

const route = useRoute();

const databaseName = computed(() => {
  return route.params.databaseName;
});

const collectionName = computed(() => {
  return route.params.collectionName;
});

const data = ref([])

const filteredData = ref([])
const loadDocuments = async () => {
  try {
    const url = `http://localhost:8080/document/getAllDocuments?databaseName=${encodeURIComponent(databaseName.value)}&collectionName=${encodeURIComponent(collectionName.value)}`;
    const response = await axios.get(url);

    data.value = response.data;

    console.log(data)
  } catch (error) {
    console.error(error);
  }
};

// Watch for changes in the route parameters
watch([databaseName, collectionName], () => {
  loadDocuments(); // Load documents when route parameters change
});

onMounted(() => {
  loadDocuments(); // Initial load of documents
});

const createDocument = async (databaseName, collectionName, documentData) => {
  try {
    const url = `http://localhost:8080/document/create?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}`;

    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    const response = await axios.post(url, documentData, { headers });
    console.log('Response Content-Type:', response.headers['content-type']);

    // Handle the response as needed
    console.log('Response:', response.data);
    await loadDocuments();
  } catch (error) {
    console.error('Error:', error);
  }
};


const deleteDocument = async (databaseName, collectionName, documentName) => {
  try {
    const url = `http://localhost:8080/document/delete?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&fileName=${encodeURIComponent(documentName)}`;


    const response = await axios.delete(url);

    // Handle the response as needed
    console.log('Response:', response.data);
    await loadDocuments();
  } catch (error) {
    console.error('Error:', error);
  }
};


const updateDocument = async (databaseName, collectionName, documentName, newDocumentData) => {
  try {
    const url = `http://localhost:8080/document/update?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&fileName=${encodeURIComponent(documentName)}`;


    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    const response = await axios.put(url, newDocumentData, { headers });
    console.log('Response Content-Type:', response.headers['content-type']);

    // Handle the response as needed
    console.log('Response:', response.data);
    await loadDocuments();
  } catch (error) {
    console.error('Error:', error);
  }
};


const indexDocumentPopup = reactive({
  show: false,
  key: ''
});

const openIndexPopup = () => {
  // Clear input fields when opening the index document popup
  indexDocumentPopup.key = '';
  indexDocumentPopup.show = true;
};

const closeIndexPopup = () => {
  indexDocumentPopup.show = false;
};


const filterDocumentPopup = reactive({
  show: false,
  key: ''
});

const openFilterPopup = () => {
  // Clear input fields when opening the filter document popup
  filterDocumentPopup.key = '';
  filterDocumentPopup.show = true;
};

const closeFilterPopup = () => {
  filterDocumentPopup.show = false;
  filteredData.value = [];
};

const submitFilterDocument = () => {
  filterByKey(databaseName.value, collectionName.value, filterDocumentPopup.key);
};

const filterDocumentValuePopup = reactive({
  show: false,
  key: '',
  value: '', // Add a value field
});

const openFilterValuePopup = () => {
  // Clear input fields when opening the filter document popup
  filterDocumentValuePopup.key = '';
  filterDocumentValuePopup.value = ''; // Initialize the value field
  filterDocumentValuePopup.show = true;
};

const closeFilterValuePopup = () => {
  filterDocumentValuePopup.show = false;
  filterDocumentValuePopup.key = ''; // Clear both key and value fields
  filterDocumentValuePopup.value = '';
  filteredData.value = [];
};

const submitFilterValueDocument = () => {
  filterByValue(databaseName.value, collectionName.value, filterDocumentValuePopup.key, filterDocumentValuePopup.value);
};


const filterByKey = async (databaseName, collectionName, key) => {
  try {
    const url = `http://localhost:8080/collection/filterByKey?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&key=${encodeURIComponent(key)}`;

    const response = await axios.get(url);

    // Handle the response as needed
    console.log('Response:', response.data);
    filteredData.value = response.data;
  } catch (error) {
    console.error('Error:', error);
  }
};

const filterByValue = async (databaseName, collectionName, key, value) => {
  try {
    const url = `http://localhost:8080/collection/filterByValue?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&key=${encodeURIComponent(key)}&value=${encodeURIComponent(value)}`;

    const response = await axios.get(url);

    // Handle the response as needed
    console.log('Response:', response.data);
    filteredData.value = response.data;
  } catch (error) {
    console.error('Error:', error);
  }
};

const showPopup = ref(false);
const jsonInput = ref('');

const createDocumentPopup = reactive({
  show: false,
  jsonInput: ''
});

const deleteDocumentPopup = reactive({
  show: false,
  documentName: ''
});

const openCreatePopup = () => {
  // Clear input fields when opening the create document popup
  createDocumentPopup.jsonInput = '';
  createDocumentPopup.show = true;
};

const closeCreatePopup = () => {
  createDocumentPopup.show = false;
};

const submitCreateDocument = () => {
  const documentData = JSON.parse(createDocumentPopup.jsonInput);
  createDocument(databaseName.value, collectionName.value, documentData);
  closeCreatePopup();
};

const openDeletePopup = () => {
  // Clear input fields when opening the delete document popup
  deleteDocumentPopup.documentName = '';
  deleteDocumentPopup.show = true;
};

const closeDeletePopup = () => {
  deleteDocumentPopup.show = false;
};

const submitDeleteDocument = () => {
  deleteDocument(databaseName.value, collectionName.value, deleteDocumentPopup.documentName);
  closeDeletePopup();
};


const updateDocumentPopup = reactive({
  show: false,
  documentName: '',
  jsonInput: ''
});

const openUpdatePopup = () => {
  // Clear input fields when opening the update document popup
  updateDocumentPopup.documentName = '';
  updateDocumentPopup.jsonInput = '';
  updateDocumentPopup.show = true;
};

const closeUpdatePopup = () => {
  updateDocumentPopup.show = false;
};

const submitUpdateDocument = () => {
  const documentData = JSON.parse(updateDocumentPopup.jsonInput);
  updateDocument(databaseName.value, collectionName.value, updateDocumentPopup.documentName, documentData);
  closeUpdatePopup();
};

// Watch for changes in the route parameters
watch([databaseName, collectionName], () => {
  loadDocuments(); // Load documents when route parameters change
});

onMounted(() => {
  loadDocuments(); // Initial load of documents
});


</script>


<template>
  <VRow>
    <VCol cols="12">
      <VCard :title= collectionName>

        <VCardText class="d-flex flex-wrap gap-4">
          <VBtn @click="openCreatePopup">Create Document</VBtn>
          <VBtn @click="openUpdatePopup">Update Document</VBtn>
          <VBtn @click="openFilterPopup">Filter By Key</VBtn>
          <VBtn @click="openFilterValuePopup">Filter By Value</VBtn>
          <VBtn @click="openDeletePopup"
                type="reset"
                color="secondary"
                variant="tonal"
          >
            Delete Document
          </VBtn>
        </VCardText>
        <ul>
          <li v-for="document in data" :key="document">
            <pre class="text-high-emphasis text-base data">{{ JSON.stringify(document, null, 2) }}</pre>
          </li>
        </ul>
      </VCard>
    </VCol>
  </VRow>

  <!-- Create Document Popup -->
  <div v-if="createDocumentPopup.show" class="popup">
    <div class="popup-content">
      <button @click="closeCreatePopup">
        <VIcon
          class="me-2"
          icon="bx-x"
          size="22"
        />
      </button>
      <h2>Create Document</h2>
      <div class="input-container">
        <textarea v-model="createDocumentPopup.jsonInput" placeholder="Document Data" class="input-field" rows="4"></textarea>
      </div>
      <div class="submit-button">
        <button @click="submitCreateDocument">Submit</button>
      </div>
    </div>
  </div>

  <!-- Delete Document Popup -->
  <div v-if="deleteDocumentPopup.show" class="popup">
    <div class="popup-content">
      <button @click="closeDeletePopup">
        <VIcon
          class="me-2"
          icon="bx-x"
          size="22"
        />
      </button>
      <h2>Delete Document</h2>
      <div class="input-container">
        <input v-model="deleteDocumentPopup.documentName" placeholder="Document Name" class="input-field" />
      </div>
      <div class="submit-button">
        <button @click="submitDeleteDocument">Submit</button>
      </div>
    </div>
  </div>

  <div v-if="updateDocumentPopup.show" class="popup">
    <div class="popup-content">
      <button @click="closeUpdatePopup">
        <VIcon
          class="me-2"
          icon="bx-x"
          size="22"
        />
      </button>
      <h2>Update Document</h2>
      <div class="input-container">
        <input v-model="updateDocumentPopup.documentName" placeholder="Document Name" class="input-field" />
      </div>
      <div class="input-container">
        <textarea v-model="updateDocumentPopup.jsonInput" placeholder="Document Data" class="input-field" rows="4"></textarea>
      </div>
      <div class="submit-button">
        <button @click="submitUpdateDocument">Submit</button>
      </div>
    </div>
  </div>

  <div v-if="filterDocumentPopup.show" class="popup">
    <div class="popup-content">
      <button @click="closeFilterPopup">
        <VIcon
          class="me-2"
          icon="bx-x"
          size="22"
        />
      </button>
      <h2>Filter Documents by Key</h2>
      <div class="input-container">
        <input v-model="filterDocumentPopup.key" placeholder="Key" class="input-field" />
      </div>
      <div class="submit-button">
        <button @click="submitFilterDocument">Submit</button>
      </div>
      <ul>
        <li v-for="document in filteredData" :key="document">
          <pre class="text-high-emphasis text-base data">{{ JSON.stringify(document, null, 2) }}</pre>
        </li>
      </ul>
    </div>
  </div>

  <div v-if="filterDocumentValuePopup.show" class="popup">
    <div class="popup-content">
      <button @click="closeFilterValuePopup">
        <VIcon
          class="me-2"
          icon="bx-x"
          size="22"
        />
      </button>
      <h2>Filter Documents by Key and Value</h2>
      <div class="input-container">
        <input v-model="filterDocumentValuePopup.key" placeholder="Key" class="input-field" />
      </div>
      <div class="input-container">
        <input v-model="filterDocumentValuePopup.value" placeholder="Value" class="input-field" />
      </div>
      <div class="submit-button">
        <button @click="submitFilterValueDocument">Submit</button>
      </div>
      <ul>
        <li v-for="document in filteredData" :key="document">
          <pre class="text-high-emphasis text-base data">{{ JSON.stringify(document, null, 2) }}</pre>
        </li>
      </ul>
    </div>
  </div>

</template>

<style lang="scss">
/* Popup styles (you can adjust as needed) */
.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.popup-content {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.data {
  padding: 20px;
}
</style>
