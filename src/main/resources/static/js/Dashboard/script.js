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
      activeItem: "services",
      groups: [],
      services: [],
      menuS: false,
      s0: "پارسو",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "تنظیمات",
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
      s22: "./settings"
    },
    created: function () {
      this.getUserInfo();
      this.getServices();
      this.getGroups();
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
        axios.get(url + "/idman/username")
        .then((res) => {
          axios.get(url + "/idman/api/users/isAdmin/" + res.data)
          .then((resp) => {
            if(resp.data){
              vm.menuS = true;
            }
          });
        });
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/idman/username")
        .then((res) => {
          vm.username = res.data;
          axios.get(url + "/idman/api/users/u/" + vm.username)
          .then((res) => {
            vm.userInfo = res.data;
            vm.name = vm.userInfo.displayName;
            vm.nameEN = vm.userInfo.firstName;
            vm.s1 = vm.name;
          });
        });
      },
      getGroups: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/idman/api/groups")
        .then((res) => {
          axios.get(url + "/idman/username")
          .then((resp) => {
            axios.get(url + "/idman/api/users/u/" + resp.data)
            .then((respo) => {
              for(let i = 0; i < respo.data.memberOf.length; ++i){
                for(let j = 0; j < res.data.length; ++j){
                  if(res.data[j].name === respo.data.memberOf[i]){
                    vm.groups.push(res.data[j]);
                  }
                }
              }
            });
          });
        });
      },
      getServices: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/idman/username")
        .then((res) => {
          axios.get(url + "/idman/api/users/u/" + res.data)
          .then((resp) => {
            axios.get(url + "/idman/api/services")
            .then((respo) => {
              for(let i = 0; i < resp.data.memberOf.length; ++i){
                for(let j = 0; j < respo.data.length; ++j){
                  for(let z = 0; z < respo.data[j].accessStrategy.requiredAttributes.member[1].length; ++z){
                    if(respo.data[j].accessStrategy.requiredAttributes.member[1][z] === resp.data.memberOf[i] && !vm.services.includes(respo.data[j])){
                      vm.services.push(respo.data[j]);
                      break;
                    }
                  }
                }
              }
            });
          });
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
          this.s7 = "Settings";
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
            this.s7 = "تنظیمات";
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
        }
      }
    }
  })
})