<template>
  <div style="height: 8rem;">
    <div class="flex align-content-center container" :dir="$store.state.direction">
      <div class="flex align-items-center justify-content-center">
        <a :href="userAvatar" target="_blank" class="avatar">
          <AppAvatar :image="userAvatar" size="xlarge" shape="circle" class="avatar"></AppAvatar>
        </a>
      </div>
      <div class="flex align-items-center justify-content-center text">
        <div>
          <div class="font-medium white-space-normal" style="font-size: 16px;">{{ $store.state.displayName }}</div>
          <div class="font-light white-space-normal">{{ $store.state.userId }}</div>
        </div>
      </div>
    </div>
    <Button type="button" class="p-button-sm p-button-danger w-full logout" icon="bx bx-log-out bx-sm" v-tooltip.top="$t('logout')" @click="logout" />
  </div>
</template>

<script>
export default {
  name: "AppUserInfo",
  data () {
    return {
      userAvatar: "images/avatarPlaceholder.png"
    }
  },
  mounted () {
    const vm = this
    this.axios({
      url: "/api/user/photo",
      method: "GET"
    }).then((res) => {
      if (res.data !== "Problem" && res.data !== "NotExist") {
        vm.userAvatar = "/api/user/photo"
      }
    })
  },
  methods: {
    logout () {
      window.location.href = "/logout"
    }
  }
}
</script>

<style scoped>
.avatar{
  width: 5.5rem;
  height: 5.5rem;
}
.container{
  background-color: #dee2e6;
  color: #495057;
  border-radius: 12px 12px 0 0;
  padding: .25rem;
}
.text{
  padding: 0 .5rem;
}
.logout{
  border-radius: 0px 0px 12px 12px;
  height: 22px;
}
</style>
