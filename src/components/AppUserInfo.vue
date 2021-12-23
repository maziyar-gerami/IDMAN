<template>
  <div class="flex align-content-center justify-content-center container" :dir="direction">
    <div class="flex align-items-center justify-content-center">
      <a :href="userAvatar" target="_blank" class="avatar">
        <AppAvatar :image="userAvatar" size="xlarge" shape="circle" class="avatar"></AppAvatar>
      </a>
    </div>
    <div class="flex align-items-center justify-content-center text">
      <div>
        <div class="font-medium white-space-normal">سید محمد جواد خوشیانی امامی</div>
        <div class="font-light white-space-normal">123456</div>
      </div>
    </div>
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
    this.axios.get("/api/user/photo")
      .then((res) => {
        if (res.data !== "Problem" && res.data !== "NotExist") {
          vm.userAvatar = "/api/user/photo"
        }
      })
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
  border-radius: 12px;
  padding: .25rem;
}
.text{
  padding: 0 .25rem;
}
</style>
