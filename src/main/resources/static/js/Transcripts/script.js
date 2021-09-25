document.addEventListener("DOMContentLoaded", function () {
  var router = new VueRouter({
    mode: "history",
    routes: []
  });
  new Vue({
    router,
    el: "#app",
    data: {
      dropdownMenu: false,
      dateNav: "",
      dateNavEn: "",
      dateNavText: "",
      recordsShownOnPage: 20,
      recordsShownOnPageReports: 20,
      currentPageReports: 1,
      totalReports: 1,
      currentPageReport: 1,
      totalReport: 1,
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "userTranscripts",
      activeItemReport: "serviceTranscriptsUsersReport",
      report: [],
      reports: [],
      reportPage: [],
      reportsPage: [],
      changePageReport: false,
      changePageReports: false,
      userPicture: "images/PlaceholderUser.png",
      reportDate: "",
      reportsDate: "",
      reportsUserId: "",
      reportMore: "",
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
      loader: false,
      overlayLoader: false,
      transcriptsUsersReportLoader: false,
      transcriptsGroupsReportLoader: false,
      transcriptsServicesReportLoader: false,
      deleteInputIcon: "left: 7%;",
      deleteInputIcon1: "left: 10%;",
      isListEmpty: false,
      isListEmpty1: false,
      s0: "احراز هویت متمرکز شرکت فلات قاره",
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
      s18: "نام",
      s19: "توضیحات",
      s20: "اتصال",
      s21: "./groups",
      s22: "./profile",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "تاریخ",
      s27: "جستجو",
      s28: "عملیات",
      s29: "برنامه",
      s30: "زمان",
      s31: "گزارش های من",
      s32: "گزارش های کاربران",
      s34: " مثال: admin ",
      s35: " مثال: 1399/05/01 ",
      s36: "./events",
      s38: "تاریخ",
      s39: "ورود موفق",
      s40: "ورود ناموفق",
      s41: "سرویس",
      s42: "سیستم عامل",
      s43: "مرورگر",
      s44: "تعداد رکورد ها: ",
      s45: "ممیزی ها",
      s46: "/audits",
      s47: "بازگشت",
      s48: "رکوردی یافت نشد",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
      publicmessagesText: "اعلان های عمومی",
      publicmessagesURLText: "./publicmessages",
      ticketingText: "پشتیبانی",
      ticketingURLText: "./ticketing",
      transcriptsText: "گزارش های جزئی",
      transcriptsURLText: "./transcripts",
      messageText: "پیام",
      userIdText: "شناسه کاربری",
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
      searchText: "جستجو...",
      allowedUsersReportSearch: "",
      bannedUsersReportSearch: "",
      allowedGroupsReportSearch: "",
      bannedGroupsReportSearch: "",
      allowedUsersReportList: [],
      bannedUsersReportList: [],
      allowedGroupsReportList: [],
      bannedGroupsReportList: [],
      displayNameFaText: "نام کامل",
      userNotFoundText: "کاربری یافت نشد",
      groupNotFoundText: "گروهی یافت نشد",
      allowedGroupsText: "گروه های دارای دسترسی",
      bannedGroupsText: "گروه های منع شده",
      enNameText: "نام انگلیسی",
      faNameText: "نام فارسی",
      reportedService: {},
      userTranscriptsAllowedServicesReportSearch: "",
      userTranscriptsBannedServicesReportSearch: "",
      userTranscriptsAllowedServicesReportList: [],
      userTranscriptsBannedServicesReportList: [],
      reportedUser: {},
      groupTranscriptsAllowedServicesReportSearch: "",
      groupTranscriptsBannedServicesReportSearch: "",
      groupTranscriptsAllowedServicesReportList: [],
      groupTranscriptsBannedServicesReportList: [],
      reportedGroup: {},
      idText: "شناسه",
      nameText: "نام",
      serviceNotFoundText: "سرویسی یافت نشد",
      allowedServicesText: "سرویس های دارای دسترسی",
      bannedServicesText: "سرویس های منع شده",
      transcriptsUserIdSearch: "",
      transcriptsGroupIdSearch: "",
      transcriptsServiceIdSearch: "",
      userReportText: "گزارش کاربر",
      groupReportText: "گزارش گروه",
      serviceReportText: "گزارش سرویس",
      faFullNameText: "نام کامل (به فارسی)",
      showText: "نمایش",
      mobileText: "شماره تلفن",
      emailText: "ایمیل",
      serviceNameText: "نام سرویس",
      serviceIdText: "شناسه سرویس",
      serviceFaNameText: "نام فارسی سرویس",
      allowedUsersText: "کاربران دارای دسترسی",
      bannedUsersText: "کاربران منع شده",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
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
      isActiveReport (menuItem) {
        return this.activeItemReport === menuItem
      },
      setActiveReport (menuItem) {
        this.activeItemReport = menuItem
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
            if(res.data.skyroomAccess){
              vm.showMeeting = true;
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
      getServiceTranscripts: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        let n = 1, m = 1;
        let tempArray = [];

        this.transcriptsServicesReportLoader = true;
        this.allowedUsersReportList = [];
        this.bannedUsersReportList = [];
        this.allowedGroupsReportList = [];
        this.bannedGroupsReportList = [];

        axios.get(url + "/api/services/" + vm.transcriptsServiceIdSearch) //
          .then((res1) => {
            vm.reportedService = res1.data;
              axios.get(url + "/api/transcripts/users/service/" + vm.transcriptsServiceIdSearch) //
                .then((res2) => {
                  if(typeof res2.data.users.licensed !== "undefined"){
                    for(let i = 0; i < res2.data.users.licensed.length; ++i){
                      res2.data.users.licensed[i].recordNumber = n;
                      vm.allowedUsersReportList.push(res2.data.users.licensed[i]);
                      n += 1;
                    }
                  }
                  n = 1;
                  if(typeof res2.data.users.unLicensed !== "undefined"){
                    for(let i = 0; i < res2.data.users.unLicensed.length; ++i){
                      res2.data.users.unLicensed[i].recordNumber = n;
                      vm.bannedUsersReportList.push(res2.data.users.unLicensed[i]);
                      n += 1;
                    }
                  }
                  n = 1;
                  axios.get(url + "/api/groups")
                    .then((res3) => {
                      if(typeof res2.data.groups.licensed !== "undefined"){
                        for(let i = 0; i < res2.data.groups.licensed.length; ++i){
                          tempArray.push(res2.data.groups.licensed[i].id);
                        }
                        for(let i = 0; i < res3.data.length; ++i){
                          if(tempArray.includes(res3.data[i].id)){
                            res3.data[i].recordNumber = n;
                            vm.allowedGroupsReportList.push(res3.data[i]);
                            n += 1;
                          }else{
                            res3.data[i].recordNumber = m;
                            vm.bannedGroupsReportList.push(res3.data[i]);
                            m += 1;
                          }
                        }
                      }else{
                        vm.allowedGroupsReportList = res3.data;
                      }
                      vm.transcriptsServicesReportLoader = false;
                    });
                });
          });
      },
      getUserTranscripts: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        let n = 1;

        this.transcriptsUsersReportLoader = true;
        this.userTranscriptsAllowedServicesReportList = [];
        this.userTranscriptsBannedServicesReportList = [];

        axios.get(url + "/api/users/u/" + vm.transcriptsUserIdSearch) //
          .then((res1) => {
          vm.reportedUser = res1.data;
          axios.get(url + "/api/transcripts/services/user/" + vm.transcriptsUserIdSearch) //
            .then((res2) => {
              if(typeof res2.data.licensed !== "undefined"){
                for(let i = 0; i < res2.data.licensed.length; ++i){
                  res2.data.licensed[i].recordNumber = n;
                  vm.userTranscriptsAllowedServicesReportList.push(res2.data.licensed[i]);
                  n += 1;
                }
              }
              n = 1;
              if(typeof res2.data.unLicensed !== "undefined"){
                for(let i = 0; i < res2.data.unLicensed.length; ++i){
                  res2.data.unLicensed[i].recordNumber = n;
                  vm.userTranscriptsBannedServicesReportList.push(res2.data.unLicensed[i]);
                  n += 1;
                }
              }
              vm.transcriptsUsersReportLoader = false;
            });
          });
      },
      getGroupTranscripts: function () {
        let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        let vm = this;
        let n = 1;

        this.transcriptsGroupsReportLoader = true;
        this.groupTranscriptsAllowedServicesReportList = [];
        this.groupTranscriptsBannedServicesReportList = [];

        axios.get(url + "/api/groups/" + vm.transcriptsGroupIdSearch) //
          .then((res1) => {
            vm.reportedGroup = res1.data;
            axios.get(url + "/api/transcripts/services/group/" + vm.transcriptsGroupIdSearch) //
              .then((res2) => {
                if(typeof res2.data.licensed !== "undefined"){
                  for(let i = 0; i < res2.data.licensed.length; ++i){
                    res2.data.licensed[i].recordNumber = n;
                    vm.groupTranscriptsAllowedServicesReportList.push(res2.data.licensed[i]);
                    n += 1;
                  }
                }
                n = 1;
                if(typeof res2.data.unLicensed !== "undefined"){
                  for(let i = 0; i < res2.data.unLicensed.length; ++i){
                    res2.data.unLicensed[i].recordNumber = n;
                    vm.groupTranscriptsBannedServicesReportList.push(res2.data.unLicensed[i]);
                    n += 1;
                  }
                }
                vm.transcriptsGroupsReportLoader = false;
              });
            });
      },
      changeLang: function () {
        if(this.lang == "EN"){
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
          this.s8 = "Quick Access";
          this.s9 = "Status Summary";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s18 = "Name";
          this.s19 = "Description";
          this.s20 = "Connect";
          this.s24 = "Configs";
          this.s26 = "Date";
          this.s27 = "Search";
          this.s28 = "Action";
          this.s29 = "Application";
          this.s30 = "Time";
          this.s31 = "My Reports";
          this.s32 = "Users Reports";
          this.s34 = "Example: admin";
          this.s35 = "Example: 1399/05/01";
          this.s38 = "Date";
          this.s39 = "Successful Login";
          this.s40 = "Failed Login";
          this.s41 = "Service";
          this.s42 = "OS";
          this.s43 = "Browser";
          this.s44 = "Records a Page: ";
          this.s45 = "Audits";
          this.s47 = "Go Back";
          this.s48 = "No Records Found";
          this.rolesText = "Roles";
          this.reportsText = "Reports";
          this.publicmessagesText = "Public Messages";
          this.ticketingText = "Ticketing";
          this.transcriptsText = "Detailed Reports";
          this.messageText = "Message";
          this.userIdText = "UserId";
          this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
          this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
          this.meetingText = "Meeting";
          this.enterMeetingText = "Enter Meeting";
          this.inviteToMeetingText = "Invite To Meeting";
          this.copyText = "Copy";
          this.returnText = "Return";
          this.searchText = "Search...";
          this.displayNameFaText = "Full Name";
          this.userNotFoundText = "User Not Found";
          this.groupNotFoundText = "Group Not Found";
          this.allowedGroupsText = "Allowed Groups";
          this.bannedGroupsText = "Banned Groups";
          this.enNameText = "English Name";
          this.faNameText = "Persian Name";
          this.idText = "ID";
          this.nameText = "Name";
          this.serviceNotFoundText = "Service Not Found";
          this.allowedServicesText = "Allowed Services";
          this.bannedServicesText = "Banned Services";
          this.userReportText = "User Report";
          this.groupReportText = "Group Report";
          this.serviceReportText = "Service Report";
          this.faFullNameText = "FullName (In Persian)";
          this.showText = "Show";
          this.mobileText = "Mobile";
          this.emailText = "Email";
          this.serviceNameText = "Service Name";
          this.serviceIdText = "Service Id";
          this.serviceFaNameText = "Service Persian Name";
          this.allowedUsersText = "Allowed Users";
          this.bannedUsersText = "Banned Users";
        }else {
            window.localStorage.setItem("lang", "FA");
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
            this.deleteInputIcon = "left: 7%;";
            this.deleteInputIcon1 = "left: 10%;";
            this.dateNavText = this.dateNav;
            this.s0 = "احراز هویت متمرکز شرکت فلات قاره";
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
            this.s18 = "نام";
            this.s19 = "توضیحات";
            this.s20 = "اتصال";
            this.s24 = "پیکربندی";
            this.s26 = "تاریخ";
            this.s27 = "جستجو";
            this.s28 = "عملیات";
            this.s29 = "برنامه";
            this.s30 = "زمان";
            this.s31 = "گزارش های من";
            this.s32 = "گزارش های کاربران";
            this.s34 = "مثال: admin";
            this.s35 = " مثال: 1399/05/01";
            this.s38 = "تاریخ";
            this.s39 = "ورود موفق";
            this.s40 = "ورود ناموفق";
            this.s41 = "سرویس";
            this.s42 = "سیستم عامل";
            this.s43 = "مرورگر";
            this.s44 = "تعداد رکورد ها: ";
            this.s45 = "ممیزی ها";
            this.s47 = "بازگشت";
            this.s48 = "رکوردی یافت نشد";
            this.rolesText = "نقش ها";
            this.reportsText = "گزارش ها";
            this.publicmessagesText = "اعلان های عمومی";
            this.ticketingText = "پشتیبانی";
            this.transcriptsText = "گزارش های جزئی";
            this.messageText = "پیام";
            this.userIdText = "شناسه کاربری";
            this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingText = "جلسه مجازی";
            this.enterMeetingText = "ورود به جلسه";
            this.inviteToMeetingText = "دعوت به جلسه";
            this.copyText = "کپی";
            this.returnText = "بازگشت";
            this.searchText = "جستجو...";
            this.displayNameFaText = "نام کامل";
            this.userNotFoundText = "کاربری یافت نشد";
            this.groupNotFoundText = "گروهی یافت نشد";
            this.allowedGroupsText = "گروه های دارای دسترسی";
            this.bannedGroupsText = "گروه های منع شده";
            this.enNameText = "نام انگلیسی";
            this.faNameText = "نام فارسی";
            this.idText = "شناسه";
            this.nameText = "نام";
            this.serviceNotFoundText = "سرویسی یافت نشد";
            this.allowedServicesText = "سرویس های دارای دسترسی";
            this.bannedServicesText = "سرویس های منع شده";
            this.userReportText = "گزارش کاربر";
            this.groupReportText = "گزارش گروه";
            this.serviceReportText = "گزارش سرویس";
            this.faFullNameText = "نام کامل (به فارسی)";
            this.showText = "نمایش";
            this.mobileText = "شماره تلفن";
            this.emailText = "ایمیل";
            this.serviceNameText = "نام سرویس";
            this.serviceIdText = "شناسه سرویس";
            this.serviceFaNameText = "نام فارسی سرویس";
            this.allowedUsersText = "کاربران دارای دسترسی";
            this.bannedUsersText = "کاربران منع شده";
        }
      },
    },

    computed:{
      allowedUsersReportListResult(){
        if(this.allowedUsersReportSearch){
          let buffer = [];
          buffer = buffer.concat(this.allowedUsersReportList.filter((item)=>{
            return this.allowedUsersReportSearch.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v))
          }));
          buffer = buffer.concat(this.allowedUsersReportList.filter((item)=>{
            return this.allowedUsersReportSearch.toLowerCase().split(" ").every(v => item.displayName.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      bannedUsersReportListResult(){
        if(this.bannedUsersReportSearch){
          let buffer = [];
          buffer = buffer.concat(this.bannedUsersReportList.filter((item)=>{
            return this.bannedUsersReportSearch.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v))
          }));
          buffer = buffer.concat(this.bannedUsersReportList.filter((item)=>{
            return this.bannedUsersReportSearch.toLowerCase().split(" ").every(v => item.displayName.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      allowedGroupsReportListResult(){
        if(this.allowedGroupsReportSearch){
          let buffer = [];
          buffer = buffer.concat(this.allowedGroupsReportList.filter((item)=>{
            return this.allowedGroupsReportSearch.toLowerCase().split(" ").every(v => item.id.toLowerCase().includes(v))
          }));
          buffer = buffer.concat(this.allowedGroupsReportList.filter((item)=>{
            return this.allowedGroupsReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      bannedGroupsReportListResult(){
        if(this.bannedGroupsReportSearch){
          let buffer = [];
          buffer = buffer.concat(this.bannedGroupsReportList.filter((item)=>{
            return this.bannedGroupsReportSearch.toLowerCase().split(" ").every(v => item.id.toLowerCase().includes(v))
          }));
          buffer = buffer.concat(this.bannedGroupsReportList.filter((item)=>{
            return this.bannedGroupsReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      userTranscriptsAllowedServicesReportListResult(){
        if(this.userTranscriptsAllowedServicesReportSearch){
          let buffer = [];
          /*buffer = buffer.concat(this.allowedServicesReportList.filter((item)=>{
              return this.allowedServicesReportSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }));*/
          buffer = buffer.concat(this.userTranscriptsAllowedServicesReportList.filter((item)=>{
            return this.userTranscriptsAllowedServicesReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      userTranscriptsBannedServicesReportListResult(){
        if(this.userTranscriptsBannedServicesReportSearch){
          let buffer = [];
          /*buffer = buffer.concat(this.bannedServicesReportList.filter((item)=>{
              return this.bannedServicesReportSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }));*/
          buffer = buffer.concat(this.userTranscriptsBannedServicesReportList.filter((item)=>{
            return this.userTranscriptsBannedServicesReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      groupTranscriptsAllowedServicesReportListResult(){
        if(this.groupTranscriptsAllowedServicesReportSearch){
          let buffer = [];
          /*buffer = buffer.concat(this.allowedServicesReportList.filter((item)=>{
              return this.allowedServicesReportSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }));*/
          buffer = buffer.concat(this.groupTranscriptsAllowedServicesReportList.filter((item)=>{
            return this.groupTranscriptsAllowedServicesReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      groupTranscriptsBannedServicesReportListResult(){
        if(this.groupTranscriptsBannedServicesReportSearch){
          let buffer = [];
          /*buffer = buffer.concat(this.bannedServicesReportList.filter((item)=>{
              return this.bannedServicesReportSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }));*/
          buffer = buffer.concat(this.groupTranscriptsBannedServicesReportList.filter((item)=>{
            return this.groupTranscriptsBannedServicesReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
    }
  })
})