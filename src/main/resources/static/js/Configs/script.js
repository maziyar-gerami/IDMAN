document.addEventListener('DOMContentLoaded', function () {
  var router = new VueRouter({
    mode: 'history',
    routes: []
  });
  new Vue({
    router,
    el: '#app',
    data: {
      dropdownMenu: false,
      dateNav: "",
      dateNavEn: "",
      dateNavText: "",
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "",
      activeItemMain: "mainConfigsPSI",
      configsList: [],
      configsDescription: [],
      configsGroupNames: [],
      restorePoints: [],
      configsLoaded: false,
      userPicture: "images/PlaceholderUser.png",
      loader: false,
      refreshSuccess: false,
      refreshSuccessText: "",
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
      s22: "./profile",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "ویرایش",
      s27: "./events",
      s28: "ذخیره پیکربندی کنونی",
      s29: "بازگردانی پیکربندی اولیه",
      s30: "نقطه بازگردانی قبلی وجود ندارد",
      s31: "بازگردانی",
      s32: "ممیزی ها",
      s33: "/audits",
      s34: "به‌روزرسانی",
      s35: "به‌روزرسانی سرویس ها",
      s36: "به‌روزرسانی کاربران",
      s37: "به‌روزرسانی CAPTCHA",
      s38: "به‌روزرسانی کلی",
      s39: "به‌روزرسانی سرویس ها با موفقیت انجام شد.",
      s40: "به‌روزرسانی کاربران با موفقیت انجام شد.",
      s41: "به‌روزرسانی CAPTCHA با موفقیت انجام شد.",
      s42: "به‌روزرسانی کلی با موفقیت انجام شد.",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
      publicmessagesText: "اعلان های عمومی",
      publicmessagesURLText: "./publicmessages",
      showMeeting: false,
      meetingInviteLinkStyle: "border-top-left-radius: 0;border-bottom-left-radius: 0;",
      meetingInviteLinkCopyStyle: "border-top-right-radius: 0;border-bottom-right-radius: 0;",
      meetingAdminLink: "",
      meetingGuestLink: "",
      meetingText: "جلسه مجازی",
      enterMeetingText: "ورود به جلسه",
      inviteToMeetingText: "دعوت به جلسه",
      copyText: "کپی",
      returnText: "بازگشت",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
      this.getConfigs();
      this.getRestorePoints();
      this.getMeetingInfo();
      if(window.localStorage.getItem("lang") === null){
        window.localStorage.setItem("lang", "FA");
      }else if(window.localStorage.getItem("lang") === "EN") {
        this.changeLang();
      }
    },
    methods: {
      setDateNav: function () {
            this.dateNav = new persianDate().format("dddd، DD MMMM YYYY");
            persianDate.toCalendar("gregorian");
            persianDate.toLocale("en");
            this.dateNavEn = new persianDate().format("dddd, DD MMMM YYYY");
            persianDate.toCalendar("persian");
            persianDate.toLocale("fa");
            this.dateNavText = this.dateNav;
      },
      dropdownNavbar: function () {
            if(this.dropdownMenu){
                let dropdowns = document.getElementsByClassName("dropdown-content");
                for (let i = 0; i < dropdowns.length; ++i) {
                    let openDropdown = dropdowns[i];
                    if(openDropdown.classList.contains("show")) {
                        openDropdown.classList.remove("show");
                    }
                }
                this.dropdownMenu = false;
            }else{
                document.getElementById("dropdownMenu").classList.toggle("show");
                this.dropdownMenu = true;
            }
      },
      openMeeting: function () {
        window.open(this.meetingAdminLink, "_blank").focus();
      },
      openOverlay: function () {
        document.getElementById("overlay").style.display = "block";
      },
      closeOverlay: function () {
        document.getElementById("overlay").style.display = "none";
      },
      copyMeetingLink: function () {
        let copyText = document.getElementById("copyMeetingLink");
        copyText.select();
        document.execCommand("copy");
        document.getElementById("copyMeetingLinkBtn").disabled = true;
        setTimeout(function(){ document.getElementById("copyMeetingLinkBtn").disabled = false; }, 3000);
      },
      getMeetingInfo: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        axios.get(url + "/api/skyroom") //
          .then((res) => {
            if(res.data.enable){
              vm.showMeeting = true;
              vm.meetingAdminLink = res.data.presenter;
              vm.meetingGuestLink = res.data.students;
            }
          });
      },
      isActive (menuItem) {
        return this.activeItem === menuItem;
      },
      setActive (menuItem) {
        this.activeItem = menuItem;
      },
      isActiveMain (menuItem) {
        return this.activeItemMain === menuItem;
      },
      setActiveMain (menuItem) {
        this.activeItemMain = menuItem;
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user") //
          .then((res) => {
            vm.username = res.data.userId;
            vm.name = res.data.displayName;
            vm.nameEN = res.data.firstName + " " + res.data.lastName;
            if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
                vm.s1 = vm.name;
            }else if(window.localStorage.getItem("lang") === "EN") {
                vm.s1 = vm.nameEN;
            }
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
        this.configsLoaded = false;
        this.configsList = [];
        this.configsDescription = [];
        this.configsGroupNames = [];
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
          this.configsLoaded = true;
        });
      },
      editConfigs: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios({
          method: 'put',
          url: url + '/api/configs',  //
          headers: {'Content-Type': 'application/json'},
          data: JSON.stringify(vm.configsList).replace(/\\\\/g, "\\")
        })
        .then((res) => {
          vm.getConfigs();
        });
      },
      getRestorePoints: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.restorePoints = [];
        axios.get(url + "/api/configs/list") //
          .then((res) => {
            for(let i = 0; i < res.data.length; ++i){
              vm.restorePoints.push(res.data[i].name);
            }
          });
      },
      saveRestorePoint: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/configs/backup") //
          .then((res) => {
            vm.getRestorePoints();
          });
      },
      restoreInitialPoint: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/configs/reset") //
          .then((res) => {
            vm.getConfigs();
          });
      },
      restorePoint: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        let name = document.getElementById("restorePointID").value;
        if(this.restorePoints.length != 0){
          axios.get(url + "/api/configs/restore/" + name) //
          .then((res) => {
            vm.getConfigs();
          });
        }else{
          alert(this.s30);
        }
      },
      refreshServices: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.loader = true;
        axios.get(url + "/api/refresh/services") //
          .then((res) => {
            vm.loader = false;
            vm.refreshSuccessText = vm.s39;
            vm.refreshSuccess = true;
            setTimeout(function(){ vm.refreshSuccess = false; }, 3000);
          });
      },
      refreshUsers: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.loader = true;
        axios.get(url + "/api/refresh/users") //
          .then((res) => {
            vm.loader = false;
            vm.refreshSuccessText = vm.s40;
            vm.refreshSuccess = true;
            setTimeout(function(){ vm.refreshSuccess = false; }, 3000);
          });
      },
      refreshCaptchas: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.loader = true;
        axios.get(url + "/api/refresh/captchas") //
          .then((res) => {
            vm.loader = false;
            vm.refreshSuccessText = vm.s41;
            vm.refreshSuccess = true;
            setTimeout(function(){ vm.refreshSuccess = false; }, 3000);
          });
      },
      refreshAll: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.loader = true;
        axios.get(url + "/api/refresh/all") //
          .then((res) => {
            vm.loader = false;
            vm.refreshSuccessText = vm.s42;
            vm.refreshSuccess = true;
            setTimeout(function(){ vm.refreshSuccess = false; }, 3000);
          });
      },
      changeLang: function () {
        if(this.lang == "EN"){
          window.localStorage.setItem("lang", "EN");
          this.margin = "margin-left: 30px;";
          this.lang = "فارسی";
          this.isRtl = false;
          this.dateNavText = this.dateNavEn;
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
          this.s17 = "ID";
          this.s18 = "Name";
          this.s19 = "Description";
          this.s20 = "Connect";
          this.s24 = "Configs";
          this.s26 = "Edit";
          this.s28 = "Backup Current Configs";
          this.s29 = "Restore Initial Configs";
          this.s30 = "There Are No Restore Points";
          this.s31 = "Restore";
          this.s32 = "Audits";
          this.s34 = "Refresh";
          this.s35 = "Refresh Services";
          this.s36 = "Refresh Users";
          this.s37 = "Refresh CAPTCHA";
          this.s38 = "Refresh All";
          this.s39 = "Services Refreshed Successfully.";
          this.s40 = "Users Refreshed Successfully.";
          this.s41 = "CAPTCHA Refreshed Successfully.";
          this.s42 = "All Refreshed Successfully.";
          this.rolesText = "Roles";
          this.reportsText = "Reports";
          this.publicmessagesText = "Public Messages";
          this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
          this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
          this.meetingText = "Meeting";
          this.enterMeetingText = "Enter Meeting";
          this.inviteToMeetingText = "Invite To Meeting";
          this.copyText = "Copy";
          this.returnText = "Return";
        }else {
            window.localStorage.setItem("lang", "FA");
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
            this.dateNavText = this.dateNav;
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
            this.s17 = "شناسه";
            this.s18 = "نام";
            this.s19 = "توضیحات";
            this.s20 = "اتصال";
            this.s24 = "پیکربندی";
            this.s26 = "ویرایش";
            this.s28 = "ذخیره پیکربندی کنونی";
            this.s29 = "بازگردانی پیکربندی اولیه";
            this.s30 = "نقطه بازگردانی قبلی وجود ندارد";
            this.s31 = "بازگردانی";
            this.s32 = "ممیزی ها";
            this.s34 = "به‌روزرسانی";
            this.s35 = "به‌روزرسانی سرویس ها";
            this.s36 = "به‌روزرسانی کاربران";
            this.s37 = "به‌روزرسانی CAPTCHA";
            this.s38 = "به‌روزرسانی کلی";
            this.s39 = "به‌روزرسانی سرویس ها با موفقیت انجام شد.";
            this.s40 = "به‌روزرسانی کاربران با موفقیت انجام شد.";
            this.s41 = "به‌روزرسانی CAPTCHA با موفقیت انجام شد.";
            this.s42 = "به‌روزرسانی کلی با موفقیت انجام شد.";
            this.rolesText = "نقش ها";
            this.reportsText = "گزارش ها";
            this.publicmessagesText = "اعلان های عمومی";
            this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingText = "جلسه مجازی";
            this.enterMeetingText = "ورود به جلسه";
            this.inviteToMeetingText = "دعوت به جلسه";
            this.copyText = "کپی";
            this.returnText = "بازگشت";
        }
      }
    },
    computed: {
      storePointsExists () {
        if(this.restorePoints.length == 0){
          return true;
        }else{
          return false;
        }
      }
    }
  })
})