<script setup>
import axios from "axios";

const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})
const deleteDatabase = async (databaseName) => {
  try {
    const url = `http://localhost:8080/database/delete?databaseName=${encodeURIComponent(databaseName)}`;
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
      <VIcon
        class="me-2"
        icon="bx-chevron-down"
        size="22"
      />

      <!-- SECTION Menu -->

      <VListItem>
        <template #prepend>
          <button @click="deleteDatabase(item.title)">
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
