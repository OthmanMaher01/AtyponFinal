<script setup>
import {ref, onMounted} from 'vue'
import {useTheme} from 'vuetify'
import axios from 'axios'
import VerticalNavSectionTitle from '@/@layouts/components/VerticalNavSectionTitle.vue'
import VerticalNavLayout from '@layouts/components/VerticalNavLayout.vue'
import VerticalNavLink from '@layouts/components/VerticalNavLink.vue'
import Footer from '@/layouts/components/Footer.vue'
import NavbarThemeSwitcher from '@/layouts/components/NavbarThemeSwitcher.vue'
import UserProfile from '@/layouts/components/UserProfile.vue'
import Create from "@/layouts/components/deleteDatabase.vue";
import PopUp from "@/layouts/components/popUp.vue";
import Databases from "@/layouts/components/Databases.vue";
import CreateCollection from "@/layouts/components/createCollection.vue";
import DeleteCollection from "@/layouts/components/deleteCollection.vue";
import DeleteDatabase from "@/layouts/components/deleteDatabase.vue";


const vuetifyTheme = useTheme()

const data = ref([/*
  {
    name: 'Database 1',
  },
  {
    name: 'Database 2',
  },*/
])

const selectedData = ref([])


const fetchData = async () => {
  try {
    const response = await axios.get('http://localhost:8080/database/getAllDatabases');

    data.value = response.data;
    console.log(data)
  } catch (error) {
    console.error(error);
  }
}

const collections = ref([]);
const selectedDatabase = ref(null);

const loadCollections = async (databaseName, index) => {
  try {
    const url = `http://localhost:8080/collection/getAllCollections?databaseName=${encodeURIComponent(databaseName)}`;
    const response = await axios.get(url);

    collections.value = Object.values(response.data);
    selectedDatabase.value = index;
  } catch (error) {
    console.error(error);
  }
};
const createDatabase = async (databaseName) => {
  try {
    const url = `http://localhost:8080/database/create?databaseName=${encodeURIComponent(databaseName)}`;
    const response = await axios.get(url);
    // Handle the response as needed
    console.log('Database created:', response.data);
  } catch (error) {
    console.error('Error creating database:', error);
  }
};
onMounted(() => {
  fetchData()
  console.log(data)
})

</script>


<template>
  <VerticalNavLayout>
    <!-- 👉 navbar -->
    <template #navbar="{ toggleVerticalOverlayNavActive }">
      <div class="d-flex h-100 align-center">
        <!-- 👉 Vertical nav toggle in overlay mode -->
        <IconBtn
          class="ms-n3 d-lg-none"
          @click="toggleVerticalOverlayNavActive(true)"
        >
          <VIcon icon="bx-menu"/>
        </IconBtn>



        <VSpacer/>


        <NavbarThemeSwitcher class="me-2"/>

        <VListItem to="/login">
          <template #prepend>
            <VIcon
              class="me-2"
              icon="bx-log-out"
              size="22"
            />
          </template>

          <VListItemTitle>Logout</VListItemTitle>
        </VListItem>
      </div>
    </template>

    <template #vertical-nav-content>
<!--      <VerticalNavLink
        :item="{
          title: 'Dashboard',
          icon: 'bx-home',
          to: '/dashboard',
        }"
      />-->

      <!-- 👉 User Interface -->
      <Databases
        :item="{
          title: 'Databases',
        }"
      />

      <div>
        <ul>
          <li v-for="(database, index) in data" :key="index">
            <!-- Assuming 'name' is the property containing the database name -->
            <button @click="loadCollections(database, index)">
              <delete-database :item="{ title: database}"/>
            </button>

            <div v-if="selectedDatabase === index">
              <ul>
                <li>
                  <create-collection :item="{ title: 'Create Collection' , parent: database}"/>
                </li>
                <li v-for="(collection, collectionIndex) in collections" :key="collectionIndex">
                  <delete-collection
                    :item="{
    title: collection,
    parent: database,
    to: {
      name: 'Collection', // Route name
      params: {
        databaseName: database,
        collectionName: collection,
      },
    },
  }"
                  />
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </div>




    </template>

    <!-- 👉 Pages -->
    <slot/>



  </VerticalNavLayout>
</template>

<style lang="scss" scoped>
.meta-key {
  border: thin solid rgba(var(--v-border-color), var(--v-border-opacity));
  border-radius: 6px;
  block-size: 1.5625rem;
  line-height: 1.3125rem;
  padding-block: 0.125rem;
  padding-inline: 0.25rem;
}
</style>
