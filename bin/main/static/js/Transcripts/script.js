document.addEventListener("DOMContentLoaded", function () {
  var router = new VueRouter({
    mode: "history",
    routes: []
  });
  Vue.component("v-pagination", window["vue-plain-pagination"])
  new Vue({
    router,
    el: "#app",
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
      activeItem: "serviceTranscripts",
      userPicture: "images/PlaceholderUser.png",
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
      serviceTranscriptsCurrentPage: 1,
      serviceTranscriptsCurrentPageBuffer: 1,
      serviceTranscriptsTotal: 1,
      serviceTranscriptsRecordsOnPage: 20,
      loader: false,
      overlayLoader: false,
      serviceTranscriptsLoader: false,
      deleteInputIcon: "left: 7%;",
      deleteInputIcon1: "left: 10%;",
      isListEmpty: false,
      isListEmpty1: false,
      s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "پروفایل",
      s10: "قوانین",
      s11: "حریم خصوصی",
      s12: "راهنما",
      s13: "کاربران",
      s14: "./dashboard",
      s15: "./services",
      s16: "./users",
      s21: "./groups",
      s22: "./profile",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s36: "./events",
      s45: "ممیزی ها",
      s46: "/audits",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
      publicMessagesText: "اعلان های عمومی",
      publicMessagesURLText: "./publicmessages",
      ticketingText: "پشتیبانی",
      ticketingURLText: "./ticketing",
      transcriptsText: "گزارش های دسترسی",
      transcriptsURLText: "./transcripts",
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
      reportedService: {},
      recordNotFoundText: "موردی یافت نشد",
      serviceIdSearch: "",
      serviceReportText: "گزارش سرویس",
      searchText: "جستجو",
      serviceNameText: "نام سرویس",
      services: [],
      serviceTranscripts: [],
      date: "تاریخ",
      time: "زمان",
      serviceTranscriptsFilterController: {
        Id: false,
        Name: true,
        DoerId: false,
        Date: false
      },
      deleteAllFiltersText: "حذف تمام فیلتر ها",
      deleteFilterText: "حذف فیلتر",
      recordsOnPageText: " رکورد در صفحه",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
      this.getServices();
      this.getServiceTranscripts();
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
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        document.getElementById("overlay").style.display = "block";
        this.overlayLoader = true;
        axios.get(url + "/api/skyroom") //
            .then((res) => {
              vm.overlayLoader = false;
              document.getElementById("overlayBody").style.display = "block";
              vm.meetingAdminLink = res.data.presenter;
              vm.meetingGuestLink = res.data.students;
            });
      },
      closeOverlay: function () {
        document.getElementById("overlay").style.display = "none";
        document.getElementById("overlayBody").style.display = "none";
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
      getUserInfo: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
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
            if(res.data.skyroomAccess){
              vm.showMeeting = true;
            }
          });
      },
      getUserPic: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        axios.get(url + "/api/user/photo") //
          .then((res) => {
            if(res.data === "Problem" || res.data === "NotExist"){
              vm.userPicture = "images/PlaceholderUser.png";
            }else{
              vm.userPicture = "/api/user/photo";
            }
          })
          .catch((error) => {
            if (error.response) {
              if (error.response.status === 400 || error.response.status === 500 || error.response.status === 403) {
                vm.userPicture = "images/PlaceholderUser.png";
              }else{
                vm.userPicture = "/api/user/photo";
              }
            }
          });
      },
      getServices: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        this.services = [];

        axios.get(url + "/api/services/main")
          .then((res) => {
            vm.services = res.data;
          });
      },
      getServiceTranscripts: function (src) {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        let date;

        if(document.getElementById("serviceTranscriptsFilterDate").value.split("/").length === 3){
          date = this.FaNumToEnNum(document.getElementById("serviceTranscriptsFilterDate").value.split("/")[2]) +
              this.FaNumToEnNum(document.getElementById("serviceTranscriptsFilterDate").value.split("/")[1]) +
              this.FaNumToEnNum(document.getElementById("serviceTranscriptsFilterDate").value.split("/")[0]);
        }else {
          date = document.getElementById("serviceTranscriptsFilterDate").value;
        }

        if(src !== "pagination"){
          this.serviceTranscriptsCurrentPageBuffer = 1;
          this.serviceTranscriptsCurrentPage = this.serviceTranscriptsCurrentPageBuffer;
        }

        this.serviceTranscriptsLoader = true;
        this.serviceTranscripts = [];

        axios.get(url + "/api/logs/reports/serviceAccess", {
          params: {
              count: vm.serviceTranscriptsRecordsOnPage,
              page: vm.serviceTranscriptsCurrentPageBuffer,
              id: document.getElementById("serviceTranscriptsFilterId").value,
              name: document.getElementById("serviceTranscriptsFilterName").value,
              doerId: document.getElementById("serviceTranscriptsFilterDoerId").value,
              date: date
          }
        }).then((res) => {
          for(let i = 0; i < res.data.reportMessageList.length; ++i){
            res.data.reportMessageList[i].recordNumber = i + 1;
          }
          vm.serviceTranscripts = res.data.reportMessageList;
          vm.serviceTranscriptsTotal = res.data.pages;
          vm.serviceTranscriptsLoader = false;
        });
      },
      changeActiveFilter: function(filter) {
        for(const prop in this.serviceTranscriptsFilterController){
          this.serviceTranscriptsFilterController[prop] = prop === filter;
        }
        let timer = setInterval(function() {
          if($("#serviceTranscriptsFilter" + filter).is(":visible")){
            $("#serviceTranscriptsFilter" + filter).focus();
            clearInterval(timer);
          }
        }, 100);
      },
      removeServiceTranscriptsFilter: function(filter) {
        if(filter === "id"){
          document.getElementById("serviceTranscriptsFilterId").value = "";
        }else if(filter === "name"){
          document.getElementById("serviceTranscriptsFilterName").selectedIndex = "0";
        }else if(filter === "doerId"){
          document.getElementById("serviceTranscriptsFilterDoerId").value = "";
        }else if(filter === "date"){
          document.getElementById("serviceTranscriptsFilterDate").value = "";
        }else if(filter === "all"){
          document.getElementById("serviceTranscriptsFilterId").value = "";
          document.getElementById("serviceTranscriptsFilterName").selectedIndex = "0";
          document.getElementById("serviceTranscriptsFilterDoerId").value = "";
          document.getElementById("serviceTranscriptsFilterDate").value = "";
        }
        this.getServiceTranscripts();
      },
      changeRecordsOnPage: function(event) {
        this.serviceTranscriptsRecordsOnPage = event.target.value;
        this.getServiceTranscripts();
        document.getElementById("serviceTranscriptsFilterCount").selectedIndex = "0";
      },
      FaNumToEnNum: function (str) {
        let s = str.split("");
        let sEn = "";
        for(let i = 0; i < s.length; ++i){
          if(s[i] === "۰" || s[i] === "0"){
            sEn = sEn + "0";
          }else if(s[i] === "۱" || s[i] === "1"){
            sEn = sEn + "1";
          }else if(s[i] === "۲" || s[i] === "2"){
            sEn = sEn + "2";
          }else if(s[i] === "۳" || s[i] === "3"){
            sEn = sEn + "3";
          }else if(s[i] === "۴" || s[i] === "4"){
            sEn = sEn + "4";
          }else if(s[i] === "۵" || s[i] === "5"){
            sEn = sEn + "5";
          }else if(s[i] === "۶" || s[i] === "6"){
            sEn = sEn + "6";
          }else if(s[i] === "۷" || s[i] === "7"){
            sEn = sEn + "7";
          }else if(s[i] === "۸" || s[i] === "8"){
            sEn = sEn + "8";
          }else if(s[i] === "۹" || s[i] === "9"){
            sEn = sEn + "9";
          }
        }
        return sEn;
      },
      addFilterEventListener: function () {
        let vm = this;
        document.getElementById("serviceTranscriptsFilterDoerId").addEventListener("keydown", function (e) {
          if (e.code === "Enter") {
            vm.getServiceTranscripts();
          }
        });
        document.getElementById("serviceTranscriptsFilterDate").addEventListener("keydown", function (e) {
          if (e.code === "Enter") {
            vm.getServiceTranscripts();
          }
        });
      },
      changeLang: function () {
        if(this.lang === "EN"){
          window.localStorage.setItem("lang", "EN");
          this.margin = "margin-left: 30px;";
          this.lang = "فارسی";
          this.isRtl = false;
          this.deleteInputIcon = "right: 7%;";
          this.deleteInputIcon1 = "right: 10%;";
          this.dateNavText = this.dateNavEn;
          this.s0 = "IOOC Centralized Authentication";
          this.s1 = this.nameEN;
          this.s2 = "Exit";
          this.s3 = "Dashboard";
          this.s4 = "Services";
          this.s5 = "Groups";
          this.s6 = "Events";
          this.s7 = "Profile";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s24 = "Configs";
          this.s45 = "Audits";
          this.rolesText = "Roles";
          this.reportsText = "Reports";
          this.publicMessagesText = "Public Messages";
          this.ticketingText = "Ticketing";
          this.transcriptsText = "Access Reports";
          this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
          this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
          this.meetingText = "Meeting";
          this.enterMeetingText = "Enter Meeting";
          this.inviteToMeetingText = "Invite To Meeting";
          this.copyText = "Copy";
          this.returnText = "Return";
          this.recordNotFoundText = "Record Not Found";
          this.serviceReportText = "Service Report";
          this.searchText = "Search";
          this.serviceNameText = "Service Name";
          this.date = "Date";
          this.time = "Time";
          this.deleteAllFiltersText = "Delete All Filters";
          this.deleteFilterText = "Delete Filter";
          this.recordsOnPageText = " Records On Page";
        }else {
            window.localStorage.setItem("lang", "FA");
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
            this.deleteInputIcon = "left: 7%;";
            this.deleteInputIcon1 = "left: 10%;";
            this.dateNavText = this.dateNav;
            this.s0 = "احراز هویت متمرکز شرکت نفت فلات قاره ایران";
            this.s1 = this.name;
            this.s2 = "خروج";
            this.s3 = "داشبورد";
            this.s4 = "سرویس ها";
            this.s5 = "گروه ها";
            this.s6 = "رویداد ها";
            this.s7 = "پروفایل";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "کاربران";
            this.s24 = "پیکربندی";
            this.s45 = "ممیزی ها";
            this.rolesText = "نقش ها";
            this.reportsText = "گزارش ها";
            this.publicMessagesText = "اعلان های عمومی";
            this.ticketingText = "پشتیبانی";
            this.transcriptsText = "گزارش های دسترسی";
            this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingText = "جلسه مجازی";
            this.enterMeetingText = "ورود به جلسه";
            this.inviteToMeetingText = "دعوت به جلسه";
            this.copyText = "کپی";
            this.returnText = "بازگشت";
            this.recordNotFoundText = "موردی یافت نشد";
            this.serviceReportText = "گزارش سرویس";
            this.searchText = "جستجو";
            this.serviceNameText = "نام سرویس";
            this.date = "تاریخ";
            this.time = "زمان";
            this.deleteAllFiltersText = "حذف تمام فیلتر ها";
            this.deleteFilterText = "حذف فیلتر";
            this.recordsOnPageText = " رکورد در صفحه";
        }
      },
    },
    watch : {
      serviceTranscriptsCurrentPage : function () {
        if(this.serviceTranscriptsCurrentPage !== this.serviceTranscriptsCurrentPageBuffer){
          this.serviceTranscriptsCurrentPageBuffer = this.serviceTranscriptsCurrentPage;
          this.getServiceTranscripts("pagination");
        }
      }
    },
    mounted() {
      this.addFilterEventListener();
    },
  })
})