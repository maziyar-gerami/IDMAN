<template>
<div class="layout-menu-container" :dir="direction">
  <AppUserInfo />
  <AppSubmenu :items="model" class="layout-menu" :root="true" @menuitem-click="onMenuItemClick" :dir="direction" />
</div>
</template>

<script>
import AppSubmenu from "@/components/AppSubmenu.vue"
import AppUserInfo from "@/components/AppUserInfo.vue"

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
  computed: {
    direction () {
      for (const language in this.$languages) {
        if (this.$i18n.locale === language) {
          return this.$languages[language]
        }
      }
      return "rtl"
    }
  },
  components: {
    AppSubmenu,
    AppUserInfo
  }
}
</script>
