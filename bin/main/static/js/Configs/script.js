function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      var i;
      for (i = 0; i < dropdowns.length; ++i) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
        }
      }
  }
} 
document.addEventListener('DOMContentLoaded', function () {
  var router = new VueRouter({
    mode: 'history',
    routes: []
  });
  new Vue({
    router,
    el: '#app',
    data: {
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "",
      configsList: [],
      configsDescription: [],
      configsGroupNames: [],
      menuS: false,
      userPicture: "images/PlaceholderUser.png",
      s0: "پارسو",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "پروفایل",
      s8: "دسترسی سریع",
      s9: "خلاصه وضعیت",
      s10: "قوانین",
      s11: "حریم خصوصی",
      s12: "راهنما",
      s13: "کاربران",
      s14: "./dashboard",
      s15: "./services",
      s16: "./users",
      s17: "شناسه",
      s18: "نام",
      s19: "توضیحات",
      s20: "اتصال",
      s21: "./groups",
      s22: "./settings",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "ویرایش",
      s27: "./events"
    },
    created: function () {
      this.getConfigs();
      this.getUserInfo();
      this.getUserPic();
      this.isAdmin();
      if(typeof this.$route.query.en !== 'undefined'){
        this.changeLang()
      }
    },
    methods: {
      isActive (menuItem) {
        return this.activeItem === menuItem
      },
      setActive (menuItem) {
        this.activeItem = menuItem
      },
      connect (add) {
        window.open(add);
      },
      isAdmin: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user/isAdmin") //
          .then((res) => {
            if(res.data){
                vm.menuS = true;
            }
          });
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user") //
          .then((res) => {
            vm.userInfo = res.data;
            vm.username = vm.userInfo.userId;
            vm.name = vm.userInfo.displayName;
            vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
            vm.s1 = vm.name;
          });
      },
      getUserPic: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user/photo") //
            .then((res) => {
              vm.userPicture = "/api/user/photo";
            })
            .catch((error) => {
                if (error.response) {
                  if (error.response.status == 400 || error.response.status == 500 || error.response.status == 403) {
                    vm.userPicture = "images/PlaceholderUser.png";
                  }else{
                    vm.userPicture = "/api/user/photo";
                  }
                }else{
                  console.log("error.response is False")
                }
            });
      },
      groupSearchDescription: function (group, description) {
        for(i = 0; i < this.configsList.length; ++i){
          if(this.configsList[i].group == group && this.configsList[i].description == description){
            return true;
          }
        }
        return false;
      },
      getConfigs: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        var j = 0;
        var z = 0;
        axios.get(url + "/api/configs") //
        .then((res) => {
          vm.configsList = res.data;
          for(var i = 0; i < vm.configsList.length; ++i){
            if(vm.configsGroupNames.indexOf(vm.configsList[i].group) == -1){
              vm.configsGroupNames[j] = vm.configsList[i].group;
              if(j == 0){
                vm.activeItem = vm.configsList[i].group;
              }
              ++j;
            }
            if(vm.configsDescription.indexOf(vm.configsList[i].description) == -1){
              vm.configsDescription[z] = vm.configsList[i].description;
              ++z;
            }
          }
        });
      },
      editConfigs: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios({
          method: 'put',
          url: url + '/api/configs',  //
          headers: {'Content-Type': 'application/json'},
          data: JSON.stringify(vm.configsList)
        })
        .then((res) => {
          location.reload();
        });
      },
      changeLang: function () {
        if(this.lang == "EN"){
          this.margin = "margin-left: 30px;";
          this.lang = "فارسی";
          this.isRtl = false;
          this.s0 = "Parsso";
          this.s1 = this.nameEN;
          this.s2 = "Exit";
          this.s3 = "Dashboard";
          this.s4 = "Services";
          this.s5 = "Groups";
          this.s6 = "Events";
          this.s7 = "Profile";
          this.s8 = "Quick Access";
          this.s9 = "Status Summary";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s14 = "./dashboard?en";
          this.s15 = "./services?en";
          this.s16 = "./users?en";
          this.s17 = "ID";
          this.s18 = "Name";
          this.s19 = "Description";
          this.s20 = "Connect";
          this.s21 = "./groups?en";
          this.s22 = "./settings?en";
          this.s23 = "./privacy?en";
          this.s24 = "Configs";
          this.s25 = "./configs?en";
          this.s26 = "Edit";
          this.s27 = "./events?en"
        } else{
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
            this.s0 = "پارسو";
            this.s1 = this.name;
            this.s2 = "خروج";
            this.s3 = "داشبورد";
            this.s4 = "سرویس ها";
            this.s5 = "گروه ها";
            this.s6 = "رویداد ها";
            this.s7 = "پروفایل";
            this.s8 = "دسترسی سریع";
            this.s9 = "خلاصه وضعیت";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "کاربران";
            this.s14 = "./dashboard";
            this.s15 = "./services";
            this.s16 = "./users";
            this.s17 = "شناسه";
            this.s18 = "نام";
            this.s19 = "توضیحات";
            this.s20 = "اتصال";
            this.s21 = "./groups";
            this.s22 = "./settings";
            this.s23 = "./privacy";
            this.s24 = "پیکربندی";
            this.s25 = "./configs";
            this.s26 = "ویرایش";
            this.s27 = "./events"
        }
      }
    }
  })
})