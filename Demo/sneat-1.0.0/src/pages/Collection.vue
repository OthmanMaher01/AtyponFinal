<script setup>
import { useRoute } from 'vue-router'
import AccountSettingsAccount from '@/views/pages/account-settings/AccountSettingsAccount.vue'
import AccountSettingsNotification from '@/views/pages/account-settings/AccountSettingsNotification.vue'
import AccountSettingsSecurity from '@/views/pages/account-settings/AccountSettingsSecurity.vue'

const route = useRoute()
const activeTab = ref(route.params.tab)
const props = defineProps({
  item: {
    type: null,
    required: true,
  },
})
// tabs
const tabs = [
  {
    title: 'Documents',
    tab: 'account',
  },
  {
    title: 'Schema',
    tab: 'security',
  },
]


</script>

<template>
  <div>
    <VTabs
      v-model="activeTab"
      show-arrows
    >
      <VTab
        v-for="item in tabs"
        :key="item.icon"
        :value="item.tab"
      >
        {{ item.title }}
      </VTab>
    </VTabs>


<!--    {{ $route.params.databaseName }}
    {{ $route.params.collectionName }}-->


    <VDivider />
    <VWindow
      v-model="activeTab"
      class="mt-5 disable-tab-transition"
    >
      <!-- Account -->
      <VWindowItem value="account">
        <AccountSettingsAccount />
      </VWindowItem>


      <!-- Security -->
      <VWindowItem value="security">
        <AccountSettingsSecurity />
      </VWindowItem>
    </VWindow>
  </div>
</template>
