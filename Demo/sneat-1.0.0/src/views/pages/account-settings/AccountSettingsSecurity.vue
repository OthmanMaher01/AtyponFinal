<script setup>
import { VDataTable } from 'vuetify/labs/VDataTable'
import axios from "axios";
import {onMounted, watch} from "vue";

const isCurrentPasswordVisible = ref(false)
const isNewPasswordVisible = ref(false)
const isConfirmPasswordVisible = ref(false)
const currentPassword = ref('12345678')
const newPassword = ref('87654321')
const confirmPassword = ref('87654321')

const passwordRequirements = [
  'Minimum 8 characters long - the more, the better',
  'At least one lowercase character',
  'At least one number, symbol, or whitespace character',
]

const serverKeys = [
  {
    name: 'Server Key 1',
    key: '23eaf7f0-f4f7-495e-8b86-fad3261282ac',
    createdOn: '28 Apr 2021, 18:20 GTM+4:10',
    permission: 'Full Access',
  },
  {
    name: 'Server Key 2',
    key: 'bb98e571-a2e2-4de8-90a9-2e231b5e99',
    createdOn: '12 Feb 2021, 10:30 GTM+2:30',
    permission: 'Read Only',
  },
  {
    name: 'Server Key 3',
    key: '2e915e59-3105-47f2-8838-6e46bf83b711',
    createdOn: '28 Dec 2020, 12:21 GTM+4:10',
    permission: 'Full Access',
  },
]

const recentDevicesHeaders = [
  {
    title: 'BROWSER',
    key: 'browser',
  },
  {
    title: 'DEVICE',
    key: 'device',
  },
  {
    title: 'LOCATION',
    key: 'location',
  },
  {
    title: 'RECENT ACTIVITY',
    key: 'recentActivity',
  },
]

const recentDevices = [
  {
    browser: 'Chrome on Windows',
    device: 'HP Spectre 360',
    location: 'New York, NY',
    recentActivity: '28 Apr 2022, 18:20',
    deviceIcon: {
      icon: 'bxl-windows',
      color: 'primary',
    },
  },
  {
    browser: 'Chrome on iPhone',
    device: 'iPhone 12x',
    location: 'Los Angeles, CA',
    recentActivity: '20 Apr 2022, 10:20',
    deviceIcon: {
      icon: 'bx-mobile',
      color: 'error',
    },
  },
  {
    browser: 'Chrome on Android',
    device: 'Oneplus 9 Pro',
    location: 'San Francisco, CA',
    recentActivity: '16 Apr 2022, 04:20',
    deviceIcon: {
      icon: 'bxl-android',
      color: 'success',
    },
  },
  {
    browser: 'Chrome on MacOS',
    device: 'Apple iMac',
    location: 'New York, NY',
    recentActivity: '28 Apr 2022, 18:20',
    deviceIcon: {
      icon: 'bxl-apple',
      color: 'secondary',
    },
  },
  {
    browser: 'Chrome on Windows',
    device: 'HP Spectre 360',
    location: 'Los Angeles, CA',
    recentActivity: '20 Apr 2022, 10:20',
    deviceIcon: {
      icon: 'bxl-windows',
      color: 'primary',
    },
  },
  {
    browser: 'Chrome on Android',
    device: 'Oneplus 9 Pro',
    location: 'San Francisco, CA',
    recentActivity: '16 Apr 2022, 04:20',
    deviceIcon: {
      icon: 'bxl-android',
      color: 'success',
    },
  },
]
import { useRoute } from 'vue-router';

const route = useRoute();

const databaseName = computed(() => {
  return route.params.databaseName;
});

const collectionName = computed(() => {
  return route.params.collectionName;
});

const schemaData = ref(null);
const schema = async () => {
  try {
    const url = `http://localhost:8080/collection/getSchema?databaseName=${encodeURIComponent(databaseName.value)}&collectionName=${encodeURIComponent(collectionName.value)}&`;
    const response = await axios.get(url);
    schemaData.value = response.data;

  } catch (error) {
    console.error(error);
  }
};

watch([databaseName, collectionName], () => {
  schema(); // Load documents when route parameters change
});

onMounted(() => {
  schema();
  console.log(schemaData)
})
</script>

<template>
  <VRow>
    <!-- SECTION: Change Password -->
    <VCol cols="12">
      <VCard>
        <pre class="text-high-emphasis text-base schema-data">{{ schemaData }}</pre>
      </VCard>
    </VCol>
  </VRow>
</template>

<style scoped>
/* Add padding to the .schema-data class */
.schema-data {
  padding: 20px; /* Adjust the value as needed */
}
</style>
