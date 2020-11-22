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
      marginServiceBox: "ml-3",
      lang: "EN",
      isRtl: true,
      activeItem: "services",
      groups: [],
      services: [],
      menuS: false,
      menuSA: false,
      userPicture: "images/PlaceholderUser.png",
      ActiveUsersChart: {
        size: 80,
        unit: "%",
        total: 100,
        sections: [
        	{ value: 0, label: "فعال", color: '#8ad4eb'},
          { value: 0, label: "غیرفعال", color: '#9467bd'},
          { value: 0, label: "قفل شده", color: '#8c564b'}
        ],
        thickness: 25,
        hasLegend: true,
        legendPlacement: "bottom"
      },
      ActiveServicesChart: {
        size: 80,
        unit: "%",
        total: 100,
        sections: [
        	{ value: 0, label: "فعال", color: '#2ca02c'},
          { value: 0, label: "غیرفعال", color: '#d62728'}
        ],
        thickness: 25,
        hasLegend: true,
        legendPlacement: "bottom"
      },
      SuccessfulLoginsChart: {
        size: 80,
        unit: "%",
        total: 100,
        sections: [
          { value: 0, label: "موفق", color: '#1f77b4'},
          { value: 0, label: "ناموفق", color: '#ff7f0e'}
        ],
        thickness: 25,
        hasLegend: true,
        legendPlacement: "bottom"
      },
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
      s26: "./events",
      s27: "لاگین های امروز"
    },
    created: function () {
      this.getUserInfo();
      this.isAdmin();
      this.getDashboardInfo();
      this.getServices();
      this.getUserPic();
      this.getGroups();
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
            if(res.data == "0"){
              vm.menuS = true;
              vm.menuSA = true;
            }else if(res.data == "1"){
                vm.menuS = true;
            }
          });
      },
      getDashboardInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/dashboard") //
          .then((res) => {
            vm.ActiveUsersChart.total = res.data.users.total;
            vm.ActiveUsersChart.sections[0].value = res.data.users.active;
            vm.ActiveUsersChart.sections[1].value = res.data.users.disabled;
            vm.ActiveUsersChart.sections[2].value = res.data.users.locked;
            
            vm.ActiveServicesChart.total = res.data.services.total;
            vm.ActiveServicesChart.sections[0].value = res.data.services.enabled;
            vm.ActiveServicesChart.sections[1].value = res.data.services.disabled;

            vm.SuccessfulLoginsChart.total = res.data.logins.total;
            vm.SuccessfulLoginsChart.sections[0].value = res.data.logins.successful;
            vm.SuccessfulLoginsChart.sections[1].value = res.data.logins.unsuccessful;
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
              if(res.data == "Problem" || res.data == "NotExist"){
                vm.userPicture = "images/PlaceholderUser.png";
              }else{
                vm.userPicture = "/api/user/photo";
              }
            })
            .catch((error) => {
                if (error.response) {
                  if (error.response.status == 400 || error.response.status == 500 || error.response.status == 403) {
                    vm.userPicture = "images/PlaceholderUser.png";
                  }else{
                    vm.userPicture = "/api/user/photo";
                  }
                }
            });
      },
      getGroups: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/groups/user") //
        .then((res) => {
          vm.groups = res.data;
        });
      },
      getServices: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/services/user") //
        .then((res) => {
          vm.services = res.data;
          for(i = 0; i < vm.services.length; ++i){
            if(typeof vm.services[i].logo !== 'undefined'){
              if(vm.services[i].logo == ""){
                vm.services[i].logo = "images/PlaceholderService.jpg";
              }
            }else{
              vm.services[i].logo = "images/PlaceholderService.jpg";
            }

            if(typeof vm.services[i].description !== 'undefined'){
              if(vm.services[i].description.length > 50){
                vm.services[i].description = vm.services[i].description.substr(0, 50) + "...";
              }
            }else{
              vm.services[i].description = "...";
            }
          }
        });
      },
      changeLang: function () {
        if(this.lang == "EN"){
          this.margin = "margin-left: 30px;";
          this.marginServiceBox = "mr-3";
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
          this.s26 = "./events?en";
          this.s27 = "Today's Logins";
          this.ActiveUsersChart.sections[0].label = "Active";
          this.ActiveUsersChart.sections[1].label = "Disabled";
          this.ActiveUsersChart.sections[2].label = "Locked";
          this.ActiveServicesChart.sections[0].label = "Active";
          this.ActiveServicesChart.sections[1].label = "Disabled";
          this.SuccessfulLoginsChart.sections[0].label = "Successful";
          this.SuccessfulLoginsChart.sections[1].label = "Unsuccessful";
        } else{
            this.margin = "margin-right: 30px;";
            this.marginServiceBox = "ml-3";
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
            this.s26 = "./events";
            this.s27 = "لاگین های امروز";
            this.ActiveUsersChart.sections[0].label = "فعال";
            this.ActiveUsersChart.sections[1].label = "غیرفعال";
            this.ActiveUsersChart.sections[2].label = "قفل شده";
            this.ActiveServicesChart.sections[0].label = "فعال";
            this.ActiveServicesChart.sections[1].label = "غیرفعال";
            this.SuccessfulLoginsChart.sections[0].label = "موفق";
            this.SuccessfulLoginsChart.sections[1].label = "ناموفق";
        }
      }
    }
  })
})