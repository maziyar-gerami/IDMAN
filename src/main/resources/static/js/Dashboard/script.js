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
      marginServiceBox: "ml-3",
      lang: "EN",
      isRtl: true,
      activeItem: "services",
      groups: [],
      services: [],
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
      s20: "ورود",
      s21: "./groups",
      s22: "./profile",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "./events",
      s27: "لاگین های امروز",
      s28: "ممیزی ها",
      s29: "/audits",
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
      this.getDashboardInfo();
      this.getServices();
      this.getUserPic();
      this.getGroups();
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
      isActive (menuItem) {
        return this.activeItem === menuItem
      },
      setActive (menuItem) {
        this.activeItem = menuItem
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
            vm.username = res.data.userId;
            vm.name = res.data.displayName;
            vm.nameEN = res.data.firstName + " " + res.data.lastName;
            if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
                vm.s1 = vm.name;
            }else if(window.localStorage.getItem("lang") === "EN") {
                vm.s1 = vm.nameEN;
            }
            if(res.data.skyRoom.enable){
                vm.showMeeting = true;
                vm.meetingAdminLink = res.data.skyRoom.presenter;
                vm.meetingGuestLink = res.data.skyRoom.students;
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
        const regex = new RegExp('^', 'g');
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

            vm.services[i].serviceId = vm.services[i].serviceId.replace(/\((.*?)\)/g, "");
            vm.services[i].serviceId = vm.services[i].serviceId.replace(/\^/g, "");

            vm.services[i].serviceId = vm.services[i].serviceId.replace(/\\/g, "\\\\")
          }
        });
      },
      changeLang: function () {
        if(this.lang == "EN"){
          window.localStorage.setItem("lang", "EN");
          this.margin = "margin-left: 30px;";
          this.marginServiceBox = "mr-3";
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
          this.s20 = "Login";
          this.s24 = "Configs";
          this.s27 = "Today's Logins";
          this.s28 = "Audits";
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
          this.ActiveUsersChart.sections[0].label = "Active";
          this.ActiveUsersChart.sections[1].label = "Disabled";
          this.ActiveUsersChart.sections[2].label = "Locked";
          this.ActiveServicesChart.sections[0].label = "Active";
          this.ActiveServicesChart.sections[1].label = "Disabled";
          this.SuccessfulLoginsChart.sections[0].label = "Successful";
          this.SuccessfulLoginsChart.sections[1].label = "Unsuccessful";
        }else {
            window.localStorage.setItem("lang", "FA");
            this.margin = "margin-right: 30px;";
            this.marginServiceBox = "ml-3";
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
            this.s20 = "ورود";
            this.s24 = "پیکربندی";
            this.s27 = "لاگین های امروز";
            this.s28 = "ممیزی ها";
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