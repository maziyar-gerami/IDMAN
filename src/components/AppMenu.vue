<template>
  <div class="layout-menu-container" :dir="$store.state.direction">
    <AppSubmenu :items="model" class="layout-menu" :root="true" @menuitem-click="onMenuItemClick" :dir="$store.state.direction" />
  </div>
</template>

<script>
import AppSubmenu from "@/components/AppSubmenu.vue"

export default {
  name: "AppMenu",
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
