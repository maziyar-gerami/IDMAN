<template>
<div class="layout-menu-container">
  <AppSubmenu :items="model" class="layout-menu" :root="true" @menuitem-click="onMenuItemClick" />
</div>
</template>

<script>
import AppSubmenu from "@/components/AppSubmenu.vue"

export default {
  props: {
    model: Array
  },
  methods: {
    onMenuItemClick (event) {
      this.$emit("menuitem-click", event)
    }
  },
  components: {
    AppSubmenu
  }
}
</script>
