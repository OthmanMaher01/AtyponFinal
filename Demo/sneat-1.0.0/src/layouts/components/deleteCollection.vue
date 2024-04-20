<script setup>
import axios from "axios";

const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})

const deleteCollection = async (databaseName, collectionName) => {
  try {
    const url = `http://localhost:8080/collection/delete?databaseName=${encodeURIComponent(databaseName)}&collectionName=${encodeURIComponent(collectionName)}&`;
    const response = await axios.get(url);

  } catch (error) {
    console.error(error);
  }
};

</script>

<template>
  <li
    class="nav-link"
    :class="{ disabled: item.disable }"
    style="margin-left: 20px;"
  >
    <Component
      :is="item.to ? 'RouterLink' : 'a'"
      :to="item.to"
      :href="item.href "
    >
      <!-- 👉 Title -->
      <span class="nav-item-title">
        {{ item.title }}
      </span>
      <VIcon
        :icon="item.icon"
        class="nav-item-icon"
      />

      <!-- SECTION Menu -->
      <VListItem>
        <template #prepend>
          <button @click="deleteCollection(item.parent, item.title)">
            <VIcon
              class="me-2"
              icon="bx-x"
              size="22"
            />
          </button>
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
</style>
