function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
  }
  window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
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
        s0: "پارسو",
        s1: "",
        s2: "خروج",
        s3: "داشبورد",
        s4: "سرویس ها",
        s5: "گروه ها",
        s6: "رویداد ها",
        s7: "تنظیمات",
        s8: "سرویس های متصل",
        s9: "سرویس های در دسترس",
        s10: "قوانین",
        s11: "حریم خصوصی",
        s12: "راهنما",
        s13: "ایجاد سرویس جدید",
        s14: "سرویس جدید",
        s15: "کاربران",
        s16: "./dashboard",
        s17: "./services",
        s18: "./createservice",
        s19: "./users"
      },
      created: function () {
        this.getUserInfo();
        if(typeof this.$route.query.en !== 'undefined'){
          this.changeLang()
        }
      },
      methods: {
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
            this.s8 = "Connected Services";
            this.s9 = "Available Services";
            this.s10 = "Rules";
            this.s11 = "Privacy";
            this.s12 = "Guide";
            this.s13 = "Create New Service";
            this.s14 = "New Service";
            this.s15 = "Users";
            this.s16 = "./dashboard?en";
            this.s17 = "./services?en";
            this.s18 = "./createservice?en";
            this.s19 = "./users?en";
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
              this.s8 = "سرویس های متصل";
              this.s9 = "سرویس های در دسترس";
              this.s10 = "قوانین";
              this.s11 = "حریم خصوصی";
              this.s12 = "راهنما";
              this.s13 = "ایجاد سرویس جدید";
              this.s14 = "سرویس جدید";
              this.s15 = "کاربران";
              this.s16 = "./dashboard";
              this.s17 = "./services";
              this.s18 = "./createservice";
              this.s19 = "./users";
          }
        }
      }
    })
  })