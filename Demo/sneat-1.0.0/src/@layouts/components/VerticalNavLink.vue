<script setup>
const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})
const moreList = [
  {
    title: 'Share',
    value: 'Share',
  },
  {
    title: 'Refresh',
    value: 'Refresh',
  },
  {
    title: 'Update',
    value: 'Update',
  },
]
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
      <VIcon
        :icon="item.icon"
        class="nav-item-icon"
      />
      <span class="nav-item-title">
        {{ item.title }}
      </span>
      <!--        <div class="me-n3 mt-n2">
                <MoreBtn :menu-list="moreList" />
              </div>-->

      <!--      <VBtn
              icon="bx-dots-vertical-rounded"
            />-->
<!--            <IconBtn class="me-2">
              <VIcon icon="bx-dots-vertical-rounded" />
            </IconBtn>-->

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
