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
  Vue.component('v-pagination', window['vue-plain-pagination'])
  new Vue({
    router,
    el: '#app',
    data: {
      recordsShownOnPage: 20,
      currentPageEvents: 1,
      totalEvents: 1,
      currentPageEvent: 1,
      totalEvent: 1,
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "myEvents",
      event: [],
      events: [],
      eventPage: [],
      eventsPage: [],
      menuS: false,
      userPicture: "images/PlaceholderUser.png",
      eventDate: "",
      eventsDate: "",
      tempEventDate: "",
      tempEventsDate: "",
      eventsUserId: "",
      bootstrapPaginationClasses: {
        ul: 'pagination',
        li: 'page-item',
        liActive: 'active',
        liDisable: 'disabled',
        button: 'page-link'  
      },
      paginationAnchorTexts: {
          first: '<<',
          prev: '<',
          next: '>',
          last: '>>'
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
      s26: "تاریخ: ",
      s27: "جستجو",
      s28: "نوع رویداد",
      s29: "برنامه",
      s30: "زمان",
      s31: "رویداد های من",
      s32: "رویداد های کاربران",
      s33: "شناسه: ",
      s34: "مثال: admin",
      s35: " مثال: 1399/05/01",
      s36: "./events",
      s37: "شناسه",
      s38: "تاریخ",
      s39: "ورود موفق",
      s40: "ورود ناموفق"
    },
    created: function () {
      this.getUserInfo();
      this.getUserPic();
      this.getEvent();
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
                vm.getEvents();
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
                  if (error.response.status == 400 || error.response.status == 500) {
                    vm.userPicture = "images/PlaceholderUser.png";
                  }else{
                    vm.userPicture = "/api/user/photo";
                  }
                }else{
                  console.log("error.response is False")
                }
            });
      },
      getEvents: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.events = [];
        axios.get(url + "/api/events")
        .then((res) => {
          for(var i = 0; i < res.data.length; ++i){
            if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
              res.data[i].action = vm.s39;
              vm.events.push(res.data[i]);
            }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
              res.data[i].action = vm.s40;
              vm.events.push(res.data[i]);
            }
          }
          vm.totalEvents = Math.ceil(vm.events.length / vm.recordsShownOnPage);
          for(let i = 0; i < vm.events.length; ++i){
            let dateArray = vm.gregorian_to_jalali(vm.events[i].time.year, vm.events[i].time.month, vm.events[i].time.day)
            vm.events[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            vm.events[i].clock = vm.events[i].time.hours + ":" + vm.events[i].time.minutes + ":" + vm.events[i].time.seconds;
          }
        });
      },
      getEvent: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.event = [];
        axios.get(url + "/api/events/user") //
        .then((res) => {
          for(var i = 0; i < res.data.length; ++i){
            if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
              res.data[i].action = vm.s39;
              vm.event.push(res.data[i]);
            }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
              res.data[i].action = vm.s40;
              vm.event.push(res.data[i]);
            }
          }
          vm.totalEvent = Math.ceil(vm.event.length / vm.recordsShownOnPage);
          for(let i = 0; i < vm.event.length; ++i){
            let dateArray = vm.gregorian_to_jalali(vm.event[i].time.year, vm.event[i].time.month, vm.event[i].time.day)
            vm.event[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            vm.event[i].clock = vm.event[i].time.hours + ":" + vm.event[i].time.minutes + ":" + vm.event[i].time.seconds;
          }
        });
      },
      getEventDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(this.tempEventDate == ""){
          this.getEvent();
        }else{
          if((this.tempEventDate.match(/\//g) || []).length == 2){
            this.event = [];
            var date = this.tempEventDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/user/date/" + vm.eventDate) //
            .then((res) => {
              for(var i = 0; i < res.data.length; ++i){
                if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
                  res.data[i].action = vm.s39;
                  vm.event.push(res.data[i]);
                }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
                  res.data[i].action = vm.s40;
                  vm.event.push(res.data[i]);
                }
              }
              vm.totalEvent = Math.ceil(vm.event.length / vm.recordsShownOnPage);
              for(let i = 0; i < vm.event.length; ++i){
                let dateArray = vm.gregorian_to_jalali(vm.event[i].time.year, vm.event[i].time.month, vm.event[i].time.day)
                vm.event[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                vm.event[i].clock = vm.event[i].time.hours + ":" + vm.event[i].time.minutes + ":" + vm.event[i].time.seconds;
              }
            });
          }else{
            console.log("Date Format is Wrong!");
          }
        }
      },
      getEventsUserId: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(this.tempEventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.tempEventsDate == "" && this.eventsUserId != ""){
          this.events = [];
          axios.get(url + "/api/events/users/" + vm.eventsUserId) //
          .then((res) => {
            for(var i = 0; i < res.data.length; ++i){
              if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
                res.data[i].action = vm.s39;
                vm.events.push(res.data[i]);
              }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
                res.data[i].action = vm.s40;
                vm.events.push(res.data[i]);
              }
            }
            vm.totalEvents = Math.ceil(vm.events.length / vm.recordsShownOnPage);
            for(let i = 0; i < vm.events.length; ++i){
              let dateArray = vm.gregorian_to_jalali(vm.events[i].time.year, vm.events[i].time.month, vm.events[i].time.day)
              vm.events[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
              vm.events[i].clock = vm.events[i].time.hours + ":" + vm.events[i].time.minutes + ":" + vm.events[i].time.seconds;
            }
          });
        }else if(this.tempEventsDate != "" && this.eventsUserId == ""){
          this.getEventsDate();
        }else if(this.tempEventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
      },
      getEventsDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(this.tempEventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.tempEventsDate == "" && this.eventsUserId != ""){
          this.getEventsUserId();
        }else if(this.tempEventsDate != "" && this.eventsUserId == ""){
          if((this.tempEventsDate.match(/\//g) || []).length == 2){
            this.events = [];
            var date = this.tempEventsDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventsDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/date/" + vm.eventsDate) //
            .then((res) => {
              for(var i = 0; i < res.data.length; ++i){
                if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
                  res.data[i].action = vm.s39;
                  vm.events.push(res.data[i]);
                }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
                  res.data[i].action = vm.s40;
                  vm.events.push(res.data[i]);
                }
              }
              vm.totalEvents = Math.ceil(vm.events.length / vm.recordsShownOnPage);
              for(let i = 0; i < vm.events.length; ++i){
                let dateArray = vm.gregorian_to_jalali(vm.events[i].time.year, vm.events[i].time.month, vm.events[i].time.day)
                vm.events[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                vm.events[i].clock = vm.events[i].time.hours + ":" + vm.events[i].time.minutes + ":" + vm.events[i].time.seconds;
              }
            });
          }else{
            console.log("Date Format is Wrong!");
          }
        }else if(this.tempEventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
      },
      getEventsUserIdDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if((this.tempEventsDate.match(/\//g) || []).length == 2){
            this.events = [];
            var date = this.tempEventsDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];s
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventsDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/users/" + vm.eventsUserId + "/date/" + vm.eventsDate) //
            .then((res) => {
              for(var i = 0; i < res.data.length; ++i){
                if(res.data[i].action == "AUTHENTICATION_SUCCESS"){
                  res.data[i].action = vm.s39;
                  vm.events.push(res.data[i]);
                }else if(res.data[i].action == "AUTHENTICATION_FAILED"){
                  res.data[i].action = vm.s40;
                  vm.events.push(res.data[i]);
                }
              }
              vm.totalEvents = Math.ceil(vm.events.length / vm.recordsShownOnPage);
              for(let i = 0; i < vm.events.length; ++i){
                let dateArray = vm.gregorian_to_jalali(vm.events[i].time.year, vm.events[i].time.month, vm.events[i].time.day)
                vm.events[i].date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                vm.events[i].clock = vm.events[i].time.hours + ":" + vm.events[i].time.minutes + ":" + vm.events[i].time.seconds;
              }
            });
          }else{
            console.log("Date Format is Wrong!");
          }
      },
      changeLang: function () {
        if(this.lang == "EN"){
          this.getEvents();
          this.getEvent();
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
          this.s26 = "Date: ";
          this.s27 = "Search";
          this.s28 = "Event Type";
          this.s29 = "Application";
          this.s30 = "Time";
          this.s31 = "My Events";
          this.s32 = "Users Events";
          this.s33 = "UserId: ";
          this.s34 = "Example: admin";
          this.s35 = "Example: 1399/05/01";
          this.s36 = "./events?en";
          this.s37 = "UserId";
          this.s38 = "Date";
          this.s39 = "Successful Authentication";
          this.s40 = "Failed Authentication";
        } else{
            this.getEvents();
            this.getEvent();
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
            this.s26 = "تاریخ: ";
            this.s27 = "جستجو";
            this.s28 = "نوع رویداد";
            this.s29 = "برنامه";
            this.s30 = "زمان";
            this.s31 = "رویداد های من";
            this.s32 = "رویداد های کاربران";
            this.s33 = "شناسه: ";
            this.s34 = "مثال: admin";
            this.s35 = " مثال: 1399/05/01";
            this.s36 = "./events";
            this.s37 = "شناسه";
            this.s38 = "تاریخ";
            this.s39 = "ورود موفق";
            this.s40 = "ورود ناموفق";
        }
      },

      div: function (a, b) {
        return parseInt((a / b));
      },
      gregorian_to_jalali: function (g_y, g_m, g_d) {
        var g_days_in_month = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        var j_days_in_month = [31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29];
        var jalali = [];
        var gy = g_y - 1600;
        var gm = g_m - 1;
        var gd = g_d - 1;
    
        var g_day_no = 365 * gy + this.div(gy + 3, 4) - this.div(gy + 99, 100) + this.div(gy + 399, 400);
    
        for (var i = 0; i < gm; ++i)
            g_day_no += g_days_in_month[i];
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)))
            g_day_no++;
        g_day_no += gd;
    
        var j_day_no = g_day_no - 79;
    
        var j_np = this.div(j_day_no, 12053);
        j_day_no = j_day_no % 12053;
    
        var jy = 979 + 33 * j_np + 4 * this.div(j_day_no, 1461);
    
        j_day_no %= 1461;
    
        if (j_day_no >= 366) {
            jy += this.div(j_day_no - 1, 365);
            j_day_no = (j_day_no - 1) % 365;
        }
        for (var i = 0; i < 11 && j_day_no >= j_days_in_month[i]; ++i)
            j_day_no -= j_days_in_month[i];
        var jm = i + 1;
        var jd = j_day_no + 1;
        jalali[0] = jy;
        jalali[1] = jm;
        jalali[2] = jd;
        return jalali;
      }
    },

    computed:{
      sortedEvent:function() {
          this.eventPage = [];
          if(this.event.length != 0){
            for(let i = 0; i < this.recordsShownOnPage; ++i){
              if(i + ((this.currentPageEvent - 1) * this.recordsShownOnPage) <= this.event.length - 1){
                  this.eventPage[i] = this.event[i + ((this.currentPageEvent - 1) * this.recordsShownOnPage)];
              }
            }
          }
          return this.eventPage;
      },
      sortedEvents:function() {
        this.eventsPage = [];
        if(this.events.length != 0){
          for(let i = 0; i < this.recordsShownOnPage; ++i){
              if(i + ((this.currentPageEvents - 1) * this.recordsShownOnPage) <= this.events.length - 1){
                  this.eventsPage[i] = this.events[i + ((this.currentPageEvents - 1) * this.recordsShownOnPage)];
              }
          }
        }
        return this.eventsPage;
      }
    }
  })
})