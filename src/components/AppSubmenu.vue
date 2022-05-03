<template>
  <ul v-if="items">
    <template v-for="(item,i) of items">
      <li v-if="visible(item) && !item.separator" :key="item.label || i" :class="[{'layout-menuitem-category': root, 'active-menuitem': activeIndex === i && !item.to && !item.disabled}]" role="none">
        <template v-if="root">
          <div class="layout-menuitem-root-text">{{item.label}}</div>
          <AppSubmenu :items="visible(item) && item.items" @menuitem-click="$emit('menuitem-click', $event)"></AppSubmenu>
        </template>
          <template v-else>
            <router-link v-if="item.to" :to="item.to" :class="[item.class, 'p-ripple', {'p-disabled': item.disabled}]" :style="item.style" @click="onMenuItemClick($event,item,i)" :target="item.target" exact role="menuitem" v-ripple>
              <i :class="item.icon"></i>
              <span>{{item.label}}</span>
              <i v-if="item.items" class="pi pi-fw pi-angle-down menuitem-toggle-icon"></i>
            </router-link>
            <a v-if="!item.to" :href="item.url||'#'" :class="[item.class, 'p-ripple', {'p-disabled': item.disabled}]" :style="item.style" @click="onMenuItemClick($event,item,i)" :target="item.target" role="menuitem" v-ripple>
              <i :class="item.icon"></i>
              <span>{{item.label}}</span>
              <i v-if="item.items" class="pi pi-fw pi-angle-down menuitem-toggle-icon"></i>
            </a>
            <transition name="layout-submenu-wrapper">
              <AppSubmenu v-show="activeIndex === i" :items="visible(item) && item.items" @menuitem-click="$emit('menuitem-click', $event)"></AppSubmenu>
            </transition>
          </template>
      </li>
      <li class="p-menu-separator" :style="item.style" v-if="visible(item) && item.separator" :key="'separator' + i" role="separator"></li>
    </template>
  </ul>
</template>
<script>
export default {
  name: "AppSubmenu",
  props: {
    items: Array,
    root: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      activeIndex: null
    }
  },
  methods: {
    onMenuItemClick (event, item, index) {
      if (item.disabled) {
        event.preventDefault()
        return
      }

      if (!item.to && !item.url) {
        event.preventDefault()
      }

      if (item.command) {
        item.command({ originalEvent: event, item: item })
      }

      if (this.$router.currentRoute._value.path === item.to) {
        this.$router.go()
      }

      this.activeIndex = index === this.activeIndex ? null : index

      this.$emit("menuitem-click", {
        originalEvent: event,
        item: item
      })
    },
    visible (item) {
      return (typeof item.visible === "function" ? item.visible() : item.visible !== false)
    }
  }
}
</script>
