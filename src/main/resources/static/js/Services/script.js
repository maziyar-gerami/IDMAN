let CheckboxDropdown = function (el) {
  let _this = this;
  this.isOpen = false;
  this.areAllChecked = false;
  this.$el = $(el);
  this.$label = this.$el.find(".dropdown-checkbox-label");
  this.$labelContainer = this.$el.find(".dropdown-checkbox-label-container");
  this.$checkAll = this.$el.find("[data-toggle='check-all']").first();
  this.$inputs = this.$el.find("[type='checkbox']");
  this.onCheckBox();
  this.$label.on("click", function(e) {
    e.preventDefault();
    _this.toggleOpen();
  });
  this.$checkAll.on("click", function(e) {
    e.preventDefault();
    _this.onCheckAll();
  });
  this.$inputs.on("change", function(e) {
    _this.onCheckBox();
  });
};
CheckboxDropdown.prototype.onCheckBox = function() {
  this.updateStatus();
};
CheckboxDropdown.prototype.updateStatus = function() {
  let checked = this.$el.find(":checked");
  let value = "";
  let text = "";
  this.areAllChecked = false;
  this.$checkAll.html(this.$checkAll.attr("checkText"));
  if(checked.length === this.$inputs.length) {
    this.areAllChecked = true;
    this.$checkAll.html(this.$checkAll.attr("uncheckText"));
  }
  for(let i = 0; i < this.$inputs.length; ++i){
    if(typeof $(this.$inputs[i]).attr("setVisible") !== typeof undefined && $(this.$inputs[i]).attr("setVisible") !== false){
      if($(this.$inputs[i]).is(":checked")){
        $("#" + $(this.$inputs[i]).attr("setVisible")).css("visibility", "visible");
      }else {
        $("#" + $(this.$inputs[i]).attr("setVisible")).css("visibility", "hidden");
      }
    }
  }
  if(checked.length > 0){
    for(let i = 0; i < checked.length - 1; ++i){
      value = value + $(checked[i]).attr("value") + ", ";
      text = text + $(checked[i]).attr("text") + ", ";
    }
    value = value + $(checked[checked.length - 1]).attr("value");
    text = text + $(checked[checked.length - 1]).attr("text");
    this.$labelContainer.html(text);
    this.$labelContainer.attr("value", value);
  }else {
    this.$labelContainer.html(this.$labelContainer.attr("text"));
    this.$labelContainer.attr("value", "");
  }
};
CheckboxDropdown.prototype.onCheckAll = function(checkAll) {
  if(!this.areAllChecked || checkAll) {
    this.areAllChecked = true;
    this.$checkAll.html(this.$checkAll.attr("uncheckText"));
    this.$inputs.prop("checked", true);
  }else {
    this.areAllChecked = false;
    this.$checkAll.html(this.$checkAll.attr("checkText"));
    this.$inputs.prop("checked", false);
  }
  this.updateStatus();
};
CheckboxDropdown.prototype.toggleOpen = function(forceOpen) {
  if(!this.isOpen || forceOpen) {
    this.isOpen = true;
    this.$el.addClass("on");
  }else {
    this.isOpen = false;
    this.$el.removeClass("on");
  }
};

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
        dropdownMenu: false,
        dateNav: "",
        dateNavEn: "",
        dateNavText: "",
        recordsShownOnPage: 20,
        userInfo: [],
        username: "",
        name: "",
        nameEN: "",
        service: {},
        services: [],
        servicesPage: [],
        searchQuery: "",
        searchQueryAllowedList: "",
        searchQueryBlockedList: "",
        userSearch:[],
        userAllowed:[],
        userBlocked:[],
        userAllowedId:[],
        userBlockedId:[],
        flagList: [true],
        indexList: [0],
        attVal: [],
        attKey: [],
        attributeList: [],
        message: "",
        editInfo: {},
        placeholder: "text-align: right;",
        margin: "margin-right: 30px;",
        lang: "EN",
        isRtl: true,
        groups: [],
        groupList: "",
        samls: false,
        oauths: false,
        editS: "display: none;",
        reportS: "display: none;",
        showS: "",
        flag: false,
        currentPage: 1,
        total: 1,
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
        userPicture: "images/PlaceholderUser.png",
        overlayLoader: false,
        exportReportLoader: false,
        usersReportLoader: false,
        groupsReportLoader: false,
        accessStrategyUsersLoader: false,
        margin1: "ml-1",
        extraInfo: {},
        accessStrategy: {},
        requiredAttributes: {},
        rejectedAttributes: {},
        multifactorPolicy: {},
        ou: [],
        ouList: [],
        userIdAllowed: [],
        userIdBlocked: [],
        contacts: [],
        contactsList: [],
        contactsObj: {},
        activeItem: "main",
        reportActiveItem: "usersReport",
        dailyAccessType: "DAY",
        metaDataAddress: true,
        metaDataFile: false,
        allIsSelected: false,
        isListEmpty: false,
        s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
        s1: "",
        s2: "خروج",
        s3: "داشبورد",
        s4: "سرویس ها",
        s5: "گروه ها",
        s6: "رویداد ها",
        s7: "پروفایل",
        s8: "سرویس های متصل",
        s9: "سرویس ها",
        s10: "قوانین",
        s11: "حریم خصوصی",
        s12: "راهنما",
        s13: "ایجاد سرویس جدید",
        s14: "جدید",
        s15: "کاربران",
        s16: "./dashboard",
        s17: "./services",
        s18: "./createservice",
        s19: "./users",
        s20: "شناسه",
        s21: "نام",
        s22: "آدرس",
        s23: "ویرایش",
        s24: "حذف",
        s25: "تنظیمات پایه",
        s26: "فعال سازی سرویس",
        s27: "نوع سرویس",
        s28: "نام سرویس",
        s29: "آدرس سرویس",
        s30: "شناسه سرویس",
        s31: "نام فارسی سرویس",
        s32: "دسترسی گروه ها",
        s33: "پیوندهای مرجع",
        s34: "آدرس لوگو",
        s35: "آدرس راهنما",
        s36: "آدرس قوانین",
        s37: "اطلاعات تماس",
        s38: "نام",
        s39: "ایمیل",
        s40: "شماره تماس",
        s41: "دپارتمان",
        s42: "تنظیمات خروج",
        s43: "آدرس خروج",
        s44: "نوع خروج",
        s45: "تایید",
        s46: "بازگشت",
        s47: "حذف",
        s48: "./groups",
        s49: "./profile",
        s50: "./privacy",
        s51: "پیکربندی",
        s52: "./configs",
        s53: "./events",
        s54: "استراتژی دسترسی",
        s55: "فعال سازی SSO",
        s56: "آدرس صفحه مقصد در صورت مجاز نبودن دسترسی",
        s57: " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)",
        s58: "تنظیمات پایه",
        s59: "استراتژی دسترسی",
        s60: "دسترسی بر اساس زمان",
        s61: "تاریخ شروع",
        s62: "زمان شروع",
        s63: "تاریخ پایان",
        s64: "زمان پایان",
        s65: "احراز هویت چند مرحله ای",
        s66: "فعال سازی MFA",
        s67: " مثال: 1399/05/01 ",
        s68: " مثال: 20:30 ",
        s69: "دسترسی کاربران",
        s70: "لیست کاربران",
        s71: "جستجو...",
        s72: "اعطا دسترسی",
        s73: "منع دسترسی",
        s74: "کاربری یافت نشد",
        s75: "جستجو کنید",
        s76: "کاربران دارای دسترسی",
        s77: "حذف",
        s78: "لیست خالی است",
        s79: "کاربران منع شده",
        s80: "دسترسی بر اساس پارامتر",
        s81: "نام پارامتر",
        s82: "مقدار پارامتر",
        s83: "حذف سرویس",
        s84: "اعمال",
        s85: "آیا از حذف سرویس های انتخاب شده اطمینان دارید؟",
        s86: "هیچ سرویسی انتخاب نشده است.",
        s87: "آیا از حذف این سرویس اطمینان دارید؟",
        s88: "آیا از حذف تمامی سرویس ها اطمینان دارید؟",
        s89: " (در صورت وارد کردن آدرس، http یا https ذکر شود)",
        s90: "آدرس",
        s91: "فایل",
        s92: "تعداد رکورد ها: ",
        s93: "موقعیت",
        s94: "ممیزی ها",
        s95: "/audits",
        s96: "دسترسی از راه دور",
        s97: "کد پاسخ های قابل قبول",
        s98: "ویرایش سرویس",
        s99: "توکن سخت افزاری",
        s100: "غیرفعال",
        s101: "سرویسی یافت نشد",
        rolesText: "نقش ها",
        rolesURLText: "./roles",
        reportsText: "گزارش ها",
        reportsURLText: "./reports",
        publicmessagesText: "اعلان های عمومی",
        publicmessagesURLText: "./publicmessages",
        ticketingText: "پشتیبانی",
        ticketingURLText: "./ticketing",
        transcriptsText: "گزارش های دسترسی",
        transcriptsURLText: "./transcripts",
        addAllGroupsText: "انتخاب همه",
        removeAllGroupsText: "لغو انتخاب همه",
        allGroupsHolderText: "انتخاب همه",
        serviceFaNameText: "نام فارسی",
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
        inputEnglishFilterText: " (تنها حروف انگلیسی و اعداد مجاز می باشند)",
        inputPersianFilterText: " (تنها حروف فارسی و اعداد مجاز می باشند)",
        dynamicOTPPassword: "رمز پویا (یکبار مصرف)",
        accessReport: "گزارش های دسترسی سرویس",
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
        serviceNotificationsText: "پیام های سرویس",
        apiAddressText: "آدرس API",
        apiKeyText: "کلید API",
        tempUserDict: {},
        userFaNameInGroupText: "نام فارسی کاربر عضو",
        userIdInGroupText: "شناسه کاربر عضو",
        selectText: "انتخاب کنید",
        selectAllText: "انتخاب همه",
        removeText: "لغو انتخاب همه",
        generateSecretText: "ایجاد رمز",
        accessByHoursAndDaysOfTheWeekText: "دسترسی بر اساس ساعات و روزهای هفته",
        saturdayText: "شنبه",
        sundayText: "یکشنبه",
        mondayText: "دوشنبه",
        tuesdayText: "سه‌شنبه",
        wednesdayText: "چهارشنبه",
        thursdayText: "پنجشنبه",
        fridayText: "جمعه",
        daysText: "روز ها",
        fromText: "از: ",
        toText: "تا: ",
        dayBasedText: "بر اساس روز",
        urlBasedText: "بر اساس آدرس",
      },
      created: function () {
        this.setDateNav();
        this.getUserInfo();
        this.getUserPic();
        this.refreshServices();
        this.getGroups();
        this.getUsersList();
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
        allSelected () {
          if(this.allIsSelected){
              this.allIsSelected = false;
              for(let i = 0; i < this.services.length; ++i){
                  if(document.getElementById("checkbox-" + this.services[i]._id).checked == true){
                      document.getElementById("checkbox-" + this.services[i]._id).click();
                  }
                  document.getElementById("row-" + this.services[i]._id).style.background = "";
              }
          }else{
              this.allIsSelected = true;
              for(let i = 0; i < this.services.length; ++i){
                  if(document.getElementById("checkbox-" + this.services[i]._id).checked == false){
                      document.getElementById("checkbox-" + this.services[i]._id).click();
                  }
                  document.getElementById("row-" + this.services[i]._id).style.background = "#c2dbff";
              }
          }
        },
        isActive (menuItem) {
          return this.activeItem === menuItem
        },
        setActive (menuItem) {
          this.activeItem = menuItem
        },
        isActiveReport (menuItem) {
          return this.reportActiveItem === menuItem
        },
        setActiveReport (menuItem) {
          this.reportActiveItem = menuItem
        },
        changeRecords: function(event) {
          this.recordsShownOnPage = event.target.value;
          this.refreshServices();
        },
        selectMetaDataAddress: function () {
          this.metaDataAddress = true;
          this.metaDataFile = false;
        },
        selectMetaDataFile: function () {
          this.metaDataAddress = false;
          this.metaDataFile = true;
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
        getUsersList: function (){
          let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          let vm = this;
          this.accessStrategyUsersLoader = true;
          axios.get(url + "/api/users")
          .then((res) => {
            vm.userSearch = res.data;
            for(let i = 0; i < vm.userSearch.length; ++i){
              vm.tempUserDict[vm.userSearch[i].userId] = vm.userSearch[i].displayName;
            }
            for(let i = 0; i < vm.userAllowed.length; ++i){
              vm.userAllowed[i].displayName = vm.tempUserDict[vm.userAllowed[i].userId];
            }
            for(let i = 0; i < vm.userBlocked.length; ++i){
              vm.userBlocked[i].displayName = vm.tempUserDict[vm.userBlocked[i].userId];
            }
            vm.accessStrategyUsersLoader = false;
          });
        },
        posInc: function(id){
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/services/position/" + id + "?value=1")
          .then((res) => {
            vm.refreshServices();
          });
        },
        posDec: function(id){
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/services/position/" + id + "?value=-1")
          .then((res) => {
            vm.refreshServices();
          });
        },
        refreshServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          this.isListEmpty = false;
          axios.get(url + "/api/services/main") //
          .then((res) => {
            if(res.data.length == 0){
              vm.isListEmpty = true;
            }
            vm.services = res.data;
            for(let i = 0; i < vm.services.length; ++i){
              vm.services[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
              vm.services[i].serviceId = vm.services[i].serviceId.replace(/\\/g, "\\\\");
            }
            vm.total = Math.ceil(vm.services.length / vm.recordsShownOnPage);
          });
        },
        showServices: function () {
          location.reload();
        },
        editServiceS: function (id) {
          this.showS = "display: none;";
          this.editS = "";
          this.reportS = "display: none;";
          let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          let vm = this;
          axios.get(url + `/api/services/${id}`) //
          .then((res) => {
            vm.service.id = res.data.id;
            
            if(res.data.accessStrategy.enabled){
              document.getElementsByName("enabled")[0].checked = true;
            }else{
              document.getElementsByName("enabled")[0].checked = false;
            }

            document.getElementsByName("id")[0].value = res.data.id;

            document.getElementsByName("name")[0].value = res.data.name;

            res.data.serviceId = res.data.serviceId.replace(/\\/g, "\\\\")
            document.getElementsByName("serviceId")[0].value = res.data.serviceId;

            if(typeof res.data.metadataLocation !== "undefined"){
              vm.samls = true;
              document.getElementsByName("metadataLocation")[0].value = res.data.metadataLocation;
            }else{
              vm.samls = false;
            }

            if(typeof res.data.clientId !== "undefined" && typeof res.data.clientSecret !== "undefined"){
              vm.oauths = true;
              document.getElementsByName("clientId")[0].value = res.data.clientId;
              document.getElementsByName("clientSecret")[0].value = res.data.clientSecret;
              let supportedGrantTypesContainer = document.getElementById("supportedGrantTypesContainer");
              let supportedGrantTypesObj =  new CheckboxDropdown(supportedGrantTypesContainer);
              let supportedResponseTypesContainer = document.getElementById("supportedResponseTypesContainer");
              let supportedResponseTypesObj =  new CheckboxDropdown(supportedResponseTypesContainer);
              if(typeof res.data.supportedGrantTypes !== "undefined"){
                for(let i = 0; i < res.data.supportedGrantTypes[1].length; ++i){
                  $(supportedGrantTypesContainer).find("[value='" + res.data.supportedGrantTypes[1][i] + "']").attr("checked","true");
                }
                supportedGrantTypesObj.updateStatus();
              }
              if(typeof res.data.supportedResponseTypes !== "undefined"){
                for(let i = 0; i < res.data.supportedResponseTypes[1].length; ++i){
                  $(supportedResponseTypesContainer).find("[value='" + res.data.supportedResponseTypes[1][i] + "']").attr("checked","true");
                }
                supportedResponseTypesObj.updateStatus();
              }
            }else{
              vm.oauths = false;
            }

            if(typeof res.data.description !== 'undefined'){
              document.getElementsByName("description")[0].value = res.data.description;
            }

            if(res.data.accessStrategy.ssoEnabled){
              document.getElementsByName("ssoEnabled")[0].checked = true;
            }else{
              document.getElementsByName("ssoEnabled")[0].checked = false;
            }

            if(typeof res.data.accessStrategy.unauthorizedRedirectUrl !== 'undefined'){
              document.getElementsByName("unauthorizedRedirectUrl")[0].value = res.data.accessStrategy.unauthorizedRedirectUrl;
            }

            if(typeof res.data.accessStrategy.startingDateTime !== 'undefined'){
              let seTime = res.data.accessStrategy.startingDateTime;
              persianDate.toCalendar('gregorian');
              let dayWrapper = new persianDate([seTime.substring(0,4), seTime.substring(5,7), seTime.substring(8,10),
                seTime.substring(11,13), seTime.substring(14,16), seTime.substring(17,19), seTime.substring(20,23)]);
              document.getElementsByName("dateStart")[0].value = dayWrapper.toCalendar('persian').format("dddd DD MMMM YYYY  HH:mm  a");
            }

            if(typeof res.data.accessStrategy.endingDateTime !== 'undefined'){
              let seTime = res.data.accessStrategy.endingDateTime;
              persianDate.toCalendar('gregorian');
              let dayWrapper = new persianDate([seTime.substring(0,4), seTime.substring(5,7), seTime.substring(8,10),
                seTime.substring(11,13), seTime.substring(14,16), seTime.substring(17,19), seTime.substring(20,23)]);
              document.getElementsByName("dateEnd")[0].value = dayWrapper.toCalendar('persian').format("dddd DD MMMM YYYY  HH:mm  a");
            }

            if(typeof res.data.logo !== 'undefined'){
              document.getElementsByName("logo")[0].value = res.data.logo;
            }

            if(typeof res.data.informationUrl !== 'undefined'){
              document.getElementsByName("informationUrl")[0].value = res.data.informationUrl;
            }

            if(typeof res.data.privacyUrl !== 'undefined'){
              document.getElementsByName("privacyUrl")[0].value = res.data.privacyUrl;
            }

            document.getElementsByName("cName")[0].value = res.data.contacts[1][0].name;

            document.getElementsByName("cEmail")[0].value = res.data.contacts[1][0].email;

            if(typeof res.data.contacts[1][0].phone !== 'undefined'){
              document.getElementsByName("cPhone")[0].value = res.data.contacts[1][0].phone;
            }
            if(typeof res.data.contacts[1][0].department !== 'undefined'){
              document.getElementsByName("cDepartment")[0].value = res.data.contacts[1][0].department;
            }

            if(res.data.logoutType == "NONE"){
              document.getElementById("option9").selected = false;
              document.getElementById("option8").selected = false;
              document.getElementById("option7").selected = true;
            }else if(res.data.logoutType == "FRONT_CHANNEL"){
              document.getElementById("option9").selected = true;
              document.getElementById("option8").selected = false;
              document.getElementById("option7").selected = false;
            }else if(res.data.logoutType == "BACK_CHANNEL"){
              document.getElementById("option9").selected = false;
              document.getElementById("option8").selected = true;
              document.getElementById("option7").selected = false;
            }

            if(typeof res.data.logoutUrl !== 'undefined'){
              document.getElementsByName("logoutUrl")[0].value = res.data.logoutUrl;
            }

            if(typeof res.data.multifactorPolicy !== 'undefined'){

              if(typeof res.data.multifactorPolicy.multifactorAuthenticationProviders !== 'undefined'){
                if(res.data.multifactorPolicy.multifactorAuthenticationProviders[0] == "java.util.LinkedHashSet" &&
                res.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] == "[\"java.util.LinkedHashSet\",[\"mfa-simple\"]]"){
                  document.getElementById("option14").selected = true;
                  document.getElementById("option15").selected = false;
                  document.getElementById("option16").selected = false;
                  document.getElementById("option17").selected = false;
                }else if(res.data.multifactorPolicy.multifactorAuthenticationProviders[0] == "java.util.LinkedHashSet" &&
                res.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] == "[\"java.util.LinkedHashSet\",[\"mfa-gauth\"]]"){
                  document.getElementById("option14").selected = false;
                  document.getElementById("option15").selected = true;
                  document.getElementById("option16").selected = false;
                  document.getElementById("option17").selected = false;
                }else if(res.data.multifactorPolicy.multifactorAuthenticationProviders[0] == "java.util.LinkedHashSet" &&
                res.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] == "[\"java.util.LinkedHashSet\",[\"mfa-u2f\"]]"){
                  document.getElementById("option14").selected = false;
                  document.getElementById("option15").selected = false;
                  document.getElementById("option16").selected = true;
                  document.getElementById("option17").selected = false;
                }else if(res.data.multifactorPolicy.multifactorAuthenticationProviders[0] == "java.util.LinkedHashSet" &&
                res.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] == "[\"java.util.LinkedHashSet\",[\"\"]]"){
                  document.getElementById("option14").selected = false;
                  document.getElementById("option15").selected = false;
                  document.getElementById("option16").selected = false;
                  document.getElementById("option17").selected = true;
                }
              }

              if(typeof res.data.multifactorPolicy.bypassEnabled !== 'undefined'){

                if(res.data.multifactorPolicy.bypassEnabled){
                  document.getElementsByName("bypassEnabled")[0].checked = true;
                }else{
                  document.getElementsByName("bypassEnabled")[0].checked = false;
                }
              }
            
              if(typeof res.data.multifactorPolicy.failureMode !== 'undefined'){
                if(res.data.multifactorPolicy.failureMode == "NONE"){
                  document.getElementById("option10").selected = true;
                  document.getElementById("option11").selected = false;
                  document.getElementById("option12").selected = false;
                  document.getElementById("option13").selected = false;
                }else if(res.data.multifactorPolicy.failureMode == "CLOSED"){
                  document.getElementById("option10").selected = false;
                  document.getElementById("option11").selected = true;
                  document.getElementById("option12").selected = false;
                  document.getElementById("option13").selected = false;
                }else if(res.data.multifactorPolicy.failureMode == "OPEN"){
                  document.getElementById("option10").selected = false;
                  document.getElementById("option11").selected = false;
                  document.getElementById("option12").selected = true;
                  document.getElementById("option13").selected = false;
                }else if(res.data.multifactorPolicy.failureMode == "PHANTOM"){
                  document.getElementById("option10").selected = false;
                  document.getElementById("option11").selected = false;
                  document.getElementById("option12").selected = false;
                  document.getElementById("option13").selected = true;
                }
              }
            }

            if(typeof res.data.accessStrategy.rejectedAttributes !== "undefined"){
              if(typeof res.data.accessStrategy.rejectedAttributes.uid !== "undefined"){
                let tempUser = {};
                if(vm.userSearch.length === 0){
                  for(let i = 0; i < res.data.accessStrategy.rejectedAttributes.uid[1].length; ++i){
                    tempUser = {};
                    tempUser.userId = res.data.accessStrategy.rejectedAttributes.uid[1][i];
                    tempUser.displayName = "";
                    this.blockUserAccess(tempUser);
                  }
                }else {
                  for(let i = 0; i < res.data.accessStrategy.rejectedAttributes.uid[1].length; ++i){
                    tempUser = {};
                    tempUser.userId = res.data.accessStrategy.rejectedAttributes.uid[1][i];
                    tempUser.displayName = vm.tempUserDict[res.data.accessStrategy.rejectedAttributes.uid[1][i]];
                    this.blockUserAccess(tempUser);
                  }
                }
              }
            }

            if(typeof res.data.accessStrategy.requiredAttributes !== "undefined"){
              let flag = false;
              let index = 0;
              const entries = Object.entries(res.data.accessStrategy.requiredAttributes);
              for(let i = 0; i < entries.length; ++i){
                if(entries[i][0] != "@class" && entries[i][0] != "ou" && entries[i][0] != "uid"){
                  if(flag){
                    this.addAttribute();
                  }
                  flag = true;
                  this.attKey[index] = entries[i][0];
                  this.attVal[index] = "";
                  for(let j = 0; j < entries[i][1][1].length-1; ++j){
                    this.attVal[index] = this.attVal[index] + entries[i][1][1][j] + ',';
                  }
                  this.attVal[index] = this.attVal[index] + entries[i][1][1][entries[i][1][1].length-1];
                  index = index + 1;
                }
              }

              if(typeof res.data.accessStrategy.requiredAttributes.uid !== "undefined"){
                let tempUser = {};
                if(vm.userSearch.length === 0){
                  for(let i = 0; i < res.data.accessStrategy.requiredAttributes.uid[1].length; ++i){
                    tempUser = {};
                    tempUser.userId = res.data.accessStrategy.requiredAttributes.uid[1][i];
                    tempUser.displayName = "";
                    this.allowUserAccess(tempUser);
                  }
                }else {
                  for(let i = 0; i < res.data.accessStrategy.requiredAttributes.uid[1].length; ++i){
                    tempUser = {};
                    tempUser.userId = res.data.accessStrategy.requiredAttributes.uid[1][i];
                    tempUser.displayName = vm.tempUserDict[res.data.accessStrategy.requiredAttributes.uid[1][i]];
                    this.allowUserAccess(tempUser);
                  }
                }
              }

              if(typeof res.data.accessStrategy.requiredAttributes.ou !== "undefined"){
                if(res.data.accessStrategy.requiredAttributes.ou[1].length == vm.groups.length){
                  document.getElementById("selectAllGroups").click();
                }else{
                  for(let i = 0; i < res.data.accessStrategy.requiredAttributes.ou[1].length; ++i){
                    document.getElementById("groupNameId" + res.data.accessStrategy.requiredAttributes.ou[1][i]).click();
                  }
                }
              }
            }

            if(typeof res.data.extraInfo !== "undefined"){
              if(typeof res.data.extraInfo.url !== "undefined"){
                document.getElementsByName("url")[0].value = res.data.extraInfo.url;
              }
              if(typeof res.data.extraInfo.notificationApiURL !== "undefined"){
                document.getElementsByName("notificationApiURL")[0].value = res.data.extraInfo.notificationApiURL;
              }

              if(typeof res.data.extraInfo.notificationApiKey !== "undefined"){
                document.getElementsByName("notificationApiKey")[0].value = res.data.extraInfo.notificationApiKey;
              }

              if(typeof res.data.extraInfo.dailyAccess !== "undefined"){
                this.dailyAccessType = "DAY";
                let tempDailyAccessFrom = "";
                let tempDailyAccessTo = "";
                if(res.data.extraInfo.dailyAccess.length > 0){
                  for(let i = 0; i < res.data.extraInfo.dailyAccess.length; ++i){
                    document.getElementById("dailyAccessCheckbox" + res.data.extraInfo.dailyAccess[i].weekDay).setAttribute("checked", "true");
                    tempDailyAccessFrom = this.EnNumToFaNum(String(res.data.extraInfo.dailyAccess[i].period.from.hour)) + ":" + this.EnNumToFaNum(String(res.data.extraInfo.dailyAccess[i].period.from.minute));
                    tempDailyAccessTo = this.EnNumToFaNum(String(res.data.extraInfo.dailyAccess[i].period.to.hour)) + ":" + this.EnNumToFaNum(String(res.data.extraInfo.dailyAccess[i].period.to.minute));
                    document.getElementById("dailyAccessStart" + res.data.extraInfo.dailyAccess[i].weekDay).setAttribute("value", tempDailyAccessFrom);
                    document.getElementById("dailyAccessEnd" + res.data.extraInfo.dailyAccess[i].weekDay).setAttribute("value", tempDailyAccessTo);
                    this.dailyAccessController(res.data.extraInfo.dailyAccess[i].weekDay);
                  }
                }
              }else if(typeof res.data.accessStrategy.endpointUrl !== "undefined"){
                this.dailyAccessType = "URL";
                document.getElementsByName("endpointUrl")[0].value = res.data.accessStrategy.endpointUrl;
                if(typeof res.data.accessStrategy.acceptableResponseCodes !== "undefined"){
                  document.getElementsByName("acceptableResponseCodes")[0].value = res.data.accessStrategy.acceptableResponseCodes;
                }
              }
            }

          });
        },
        editService: function (id) {
          let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          let vm = this;

          let serviceIdFlag = false;
          for(let i = 0; i < document.getElementsByName('serviceId')[0].value.length; ++i){
            if(i == 0){
              if(document.getElementsByName('serviceId')[0].value[i] == '\\' && 
              document.getElementsByName('serviceId')[0].value[i+1] != '\\'){
                serviceIdFlag = true;
                break;
              }
            }else if(i == document.getElementsByName('serviceId')[0].value.length-1){
              if(document.getElementsByName('serviceId')[0].value[i] == '\\' && 
              document.getElementsByName('serviceId')[0].value[i-1] != '\\'){
                serviceIdFlag = true;
                break;
              }
            }else{
              if(document.getElementsByName('serviceId')[0].value[i] == '\\' && 
              document.getElementsByName('serviceId')[0].value[i+1] != '\\' &&
              document.getElementsByName('serviceId')[0].value[i-1] != '\\'){
                serviceIdFlag = true;
                break;
              }
            }
          }

          if(document.getElementsByName('name')[0].value == "" || document.getElementsByName('serviceId')[0].value == "" ||
            document.getElementsByName('cName')[0].value == "" || document.getElementsByName('cEmail')[0].value == "" ||
            document.getElementsByName('description')[0].value == "" || document.getElementsByName('url')[0].value == ""){
            alert("لطفا قسمت های الزامی را پر کنید.");
          }else if(serviceIdFlag){
            alert("فرمت آدرس سرویس درست نمی باشد.");
          }else{

            this.service.name = document.getElementsByName('name')[0].value;

            this.service.serviceId = document.getElementsByName('serviceId')[0].value;

            if(document.getElementsByName('description')[0].value != ""){
              this.service.description = document.getElementsByName('description')[0].value;
            }else{
              this.service.description = null;
            }

            this.extraInfo.url = document.getElementsByName('url')[0].value;

            if(document.getElementsByName('informationUrl')[0].value != ""){
              this.service.informationUrl = document.getElementsByName('informationUrl')[0].value;
            }else{
              this.service.informationUrl = null;
            }

            if(document.getElementsByName('privacyUrl')[0].value != ""){
              this.service.privacyUrl = document.getElementsByName('privacyUrl')[0].value;
            }else{
              this.service.privacyUrl = null;
            }

            if(document.getElementsByName('logoutType')[0].value != ""){
              this.service.logoutType = document.getElementsByName('logoutType')[0].value;
            }else{
              this.service.logoutType = null;
            }

            if(document.getElementsByName('logoutUrl')[0].value != ""){
              this.service.logoutUrl = document.getElementsByName('logoutUrl')[0].value;
            }else{
              this.service.logoutUrl = null;
            }

            if(document.getElementsByName('enabled')[0].checked){
              this.accessStrategy.enabled = true;
            }else{
              this.accessStrategy.enabled = false;
            }

            if(document.getElementsByName('ssoEnabled')[0].checked){
              this.accessStrategy.ssoEnabled = true;
            }else{
              this.accessStrategy.ssoEnabled = false;
            }

            if(document.getElementsByName('unauthorizedRedirectUrl')[0].value != ""){
              this.accessStrategy.unauthorizedRedirectUrl = document.getElementsByName('unauthorizedRedirectUrl')[0].value;
            }else{
              this.accessStrategy.unauthorizedRedirectUrl = null;
            }

            if(document.getElementsByName('dateStart')[0].value != "" && 
            document.getElementsByName('dateEnd')[0].value != ""){
                let dateStartTemp = document.getElementsByName('dateStart')[0].value.split("  ");
                let dateStart = dateStartTemp[0].split(' ');
                let dateStartFinal;
                dateStart[dateStart.length-1] = this.FaNumToEnNum(dateStart[dateStart.length-1]);
                dateStart[dateStart.length-3] = this.FaNumToEnNum(dateStart[dateStart.length-3]);

                switch(dateStart[dateStart.length-2]) {
                  case "فروردین":
                    dateStartFinal = dateStart[dateStart.length-1] + "-01-" + dateStart[dateStart.length-3];
                    break;
                  case "اردیبهشت":
                    dateStartFinal = dateStart[dateStart.length-1] + "-02-" + dateStart[dateStart.length-3];
                    break;
                  case "خرداد":
                    dateStartFinal = dateStart[dateStart.length-1] + "-03-" + dateStart[dateStart.length-3];
                    break;
                  case "تیر":
                    dateStartFinal = dateStart[dateStart.length-1] + "-04-" + dateStart[dateStart.length-3];
                    break;
                  case "مرداد":
                    dateStartFinal = dateStart[dateStart.length-1] + "-05-" + dateStart[dateStart.length-3];
                    break;
                  case "شهریور":
                    dateStartFinal = dateStart[dateStart.length-1] + "-06-" + dateStart[dateStart.length-3];
                    break;
                  case "مهر":
                    dateStartFinal = dateStart[dateStart.length-1] + "-07-" + dateStart[dateStart.length-3];
                    break;
                  case "آبان":
                    dateStartFinal = dateStart[dateStart.length-1] + "-08-" + dateStart[dateStart.length-3];
                    break;
                  case "آذر":
                    dateStartFinal = dateStart[dateStart.length-1] + "-09-" + dateStart[dateStart.length-3];
                    break;
                  case "دی":
                    dateStartFinal = dateStart[dateStart.length-1] + "-10-" + dateStart[dateStart.length-3];
                    break;
                  case "بهمن":
                    dateStartFinal = dateStart[dateStart.length-1] + "-11-" + dateStart[dateStart.length-3];
                    break;
                  case "اسفند":
                    dateStartFinal = dateStart[dateStart.length-1] + "-12-" + dateStart[dateStart.length-3];
                    break;
                  default:
                    console.log("Wrong Input for Month");
                }

                let dateEndTemp = document.getElementsByName('dateEnd')[0].value.split("  ");
                let dateEnd = dateEndTemp[0].split(' ');
                let dateEndFinal;
                dateEnd[dateEnd.length-1] = this.FaNumToEnNum(dateEnd[dateEnd.length-1]);
                dateEnd[dateEnd.length-3] = this.FaNumToEnNum(dateEnd[dateEnd.length-3]);

                switch(dateEnd[dateEnd.length-2]) {
                  case "فروردین":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-01-" + dateEnd[dateEnd.length-3];
                    break;
                  case "اردیبهشت":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-02-" + dateEnd[dateEnd.length-3];
                    break;
                  case "خرداد":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-03-" + dateEnd[dateEnd.length-3];
                    break;
                  case "تیر":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-04-" + dateEnd[dateEnd.length-3];
                    break;
                  case "مرداد":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-05-" + dateEnd[dateEnd.length-3];
                    break;
                  case "شهریور":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-06-" + dateEnd[dateEnd.length-3];
                    break;
                  case "مهر":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-07-" + dateEnd[dateEnd.length-3];
                    break;
                  case "آبان":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-08-" + dateEnd[dateEnd.length-3];
                    break;
                  case "آذر":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-09-" + dateEnd[dateEnd.length-3];
                    break;
                  case "دی":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-10-" + dateEnd[dateEnd.length-3];
                    break;
                  case "بهمن":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-11-" + dateEnd[dateEnd.length-3];
                    break;
                  case "اسفند":
                    dateEndFinal = dateEnd[dateEnd.length-1] + "-12-" + dateEnd[dateEnd.length-3];
                    break;
                  default:
                    console.log("Wrong Input for Month");
                }

                let timeStart = dateStartTemp[1].split(':');
                let timeEnd = dateEndTemp[1].split(':');

                timeStart = this.FaNumToEnNum(timeStart[0]) + ':' + this.FaNumToEnNum(timeStart[1]);
                timeEnd = this.FaNumToEnNum(timeEnd[0]) + ':' + this.FaNumToEnNum(timeEnd[1]);
                
                let dateS = dateStartFinal.split('-');
                let dateE = dateEndFinal.split('-');

                if(parseInt(dateS[1]) < 7){
                  timeStart = timeStart + ":00.000+04:30";
                }else{
                  timeStart = timeStart + ":00.000+03:30";
                }

                if(parseInt(dateE[1]) < 7){
                  timeEnd = timeEnd + ":00.000+04:30";
                }else{
                  timeEnd = timeEnd + ":00.000+03:30";
                }

                let TempS = timeStart.split(':');
                let TempE = timeEnd.split(':');
                if(TempS[0].length == 1){
                  TempS[0] = '0' + TempS[0];
                  timeStart = "";
                  for(i = 0; i < TempS.length; ++i){
                    timeStart = timeStart + TempS[i] + ':';
                  }
                  timeStart = timeStart.substring(0,timeStart.length-1);
                }
                if(TempE[0].length == 1){
                  TempE[0] = '0' + TempE[0];
                  timeEnd = "";
                  for(i = 0; i < TempE.length; ++i){
                    timeEnd = timeEnd + TempE[i] + ':';
                  }
                  timeEnd = timeEnd.substring(0,timeEnd.length-1);
                }

                TempS = dateStartFinal.split('-');
                TempE = dateEndFinal.split('-');
                if(TempS[1].length == 1){
                  TempS[1] = '0' + TempS[1];
                }
                if(TempS[2].length == 1){
                  TempS[2] = '0' + TempS[2];
                }
                if(TempE[1].length == 1){
                  TempE[1] = '0' + TempE[1];
                }
                if(TempE[2].length == 1){
                  TempE[2] = '0' + TempE[2];
                }
                
                dateStartFinal = TempS[0] + '-' + TempS[1] + '-' + TempS[2];
                dateEndFinal = TempE[0] + '-' + TempE[1] + '-' + TempE[2];

                this.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart;
                this.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd;
            }

            if(document.getElementsByName('groups')[0].value != ""){
              this.ouList = document.getElementsByName('groups')[0].value.split(',');
              this.ou[0] = "java.util.HashSet";
              this.ou[1] = this.ouList;
              this.requiredAttributes.ou = this.ou;
            }

            if(this.userAllowedId.length != 0){
              this.userIdAllowed[0] = "java.util.HashSet";
              this.userIdAllowed[1] = this.userAllowedId;
              this.requiredAttributes.uid = this.userIdAllowed;
            }
            
            if(this.userBlockedId.length != 0){
              this.userIdBlocked[0] = "java.util.HashSet";
              this.userIdBlocked[1] = this.userBlockedId;
              this.rejectedAttributes.uid = this.userIdBlocked;
            }
            
            for(i = 0; i < this.indexList.length; ++i){
              if(this.flagList[i]){
                if(document.getElementById("attributeKey" + this.indexList[i]).value != "" &&
                document.getElementById("attributeValue" + this.indexList[i]).value != ""){
                  if(document.getElementById("attributeKey" + this.indexList[i]).value != "@class" &&
                  document.getElementById("attributeKey" + this.indexList[i]).value != "uid" &&
                  document.getElementById("attributeKey" + this.indexList[i]).value != "ou"){
                    this.requiredAttributes[document.getElementById("attributeKey" + this.indexList[i]).value] = ["java.util.HashSet", document.getElementById("attributeValue" + this.indexList[i]).value.split(',')];
                  }
                }
              }
            }
  
            this.accessStrategy.requiredAttributes = this.requiredAttributes;
            this.accessStrategy.rejectedAttributes = this.rejectedAttributes;
            
            
            this.contactsObj.name = document.getElementsByName('cName')[0].value;
            
            this.contactsObj.email = document.getElementsByName('cEmail')[0].value;

            if(document.getElementsByName('cPhone')[0].value != ""){
              this.contactsObj.phone = document.getElementsByName('cPhone')[0].value;
            }else{
              this.contactsObj.phone = null;
            }

            if(document.getElementsByName('cDepartment')[0].value != ""){
              this.contactsObj.department = document.getElementsByName('cDepartment')[0].value;
            }else{
              this.contactsObj.department = null;
            }
            
            this.contactsList[0] = this.contactsObj;
            this.contacts[0] = "java.util.ArrayList";
            this.contacts[1] = this.contactsList;

            if(document.getElementsByName('mfaEnabled')[0].value != ""){
              this.multifactorPolicy.multifactorAuthenticationProviders = document.getElementsByName('mfaEnabled')[0].value;
            }else{
              this.multifactorPolicy.multifactorAuthenticationProviders = "";
            }

            if(document.getElementsByName('bypassEnabled')[0].checked){
              this.multifactorPolicy.bypassEnabled = true;
            }else{
              this.multifactorPolicy.bypassEnabled = false;
            }

            if(document.getElementsByName('failureMode')[0].value != ""){
              this.multifactorPolicy.failureMode = document.getElementsByName('failureMode')[0].value;
            }else{
              this.multifactorPolicy.failureMode = null;
            }

            if(document.getElementsByName("notificationApiURL")[0].value != ""){
              this.extraInfo.notificationApiURL = document.getElementsByName("notificationApiURL")[0].value;
            }else{
              this.extraInfo.notificationApiURL = null;
            }

            if(document.getElementsByName("notificationApiKey")[0].value != ""){
              this.extraInfo.notificationApiKey = document.getElementsByName("notificationApiKey")[0].value;
            }else{
              this.extraInfo.notificationApiKey = null;
            }

            if(this.dailyAccessType === "DAY"){
              let dailyAccessArray = document.getElementsByName("dailyAccess")[0].getAttribute("value").split("");
              if(dailyAccessArray.length !== 0){
                this.extraInfo.dailyAccess = [];
                let tempDailyAccessDay = {};
                let tempDailyAccessStart = "";
                let tempDailyAccessEnd = "";
                for(let i = 0; i < dailyAccessArray.length; ++i){
                  if(typeof document.getElementById("dailyAccessStart" + dailyAccessArray[i]) !== "undefined" &&
                      typeof document.getElementById("dailyAccessEnd" + dailyAccessArray[i]) !== "undefined"){
                    tempDailyAccessStart = document.getElementById("dailyAccessStart" + dailyAccessArray[i]).value.split(":");
                    tempDailyAccessEnd =  document.getElementById("dailyAccessEnd" + dailyAccessArray[i]).value.split(":");
                    if(tempDailyAccessStart[0] === ""){
                      tempDailyAccessStart[0] = "0";
                      tempDailyAccessStart.push("00");
                    }else if(typeof tempDailyAccessStart[1] === "undefined"){
                      tempDailyAccessStart.push("00");
                    }
                    if(tempDailyAccessEnd[0] === ""){
                      tempDailyAccessEnd[0] = "23";
                      tempDailyAccessEnd.push("59");
                    }else if(typeof tempDailyAccessEnd[1] === "undefined"){
                      tempDailyAccessEnd.push("00");
                    }
                    tempDailyAccessStart[0] = this.FaNumToEnNum(tempDailyAccessStart[0]);
                    tempDailyAccessStart[1] = this.FaNumToEnNum(tempDailyAccessStart[1]);
                    tempDailyAccessEnd[0] = this.FaNumToEnNum(tempDailyAccessEnd[0]);
                    tempDailyAccessEnd[1] = this.FaNumToEnNum(tempDailyAccessEnd[1]);
                    tempDailyAccessDay = {
                      "weekDay": dailyAccessArray[i],
                      "period": {
                        "from": {
                          "hour": tempDailyAccessStart[0],
                          "minute": tempDailyAccessStart[1]
                        },
                        "to": {
                          "hour": tempDailyAccessEnd[0],
                          "minute": tempDailyAccessEnd[1]
                        }
                      }
                    }
                    this.extraInfo.dailyAccess.push(tempDailyAccessDay);
                  }else {
                    this.extraInfo.dailyAccess = null;
                  }
                }
              }else {
                this.extraInfo.dailyAccess = null;
              }
            }else if(this.dailyAccessType === "URL"){
              if(document.getElementsByName("endpointUrl")[0].value !== ""){
                this.accessStrategy.endpointUrl = document.getElementsByName("endpointUrl")[0].value;
              }else{
                this.accessStrategy.endpointUrl = null;
              }

              if(document.getElementsByName("acceptableResponseCodes")[0].value !== ""){
                this.accessStrategy.acceptableResponseCodes = document.getElementsByName("acceptableResponseCodes")[0].value;
              }else{
                if(document.getElementsByName("endpointUrl")[0].value !== ""){
                  this.accessStrategy.acceptableResponseCodes = "200";
                }else{
                  this.accessStrategy.acceptableResponseCodes = null;
                }
              }
            }


            let logoFile = document.querySelector("#logoFile");
            if(logoFile.files[0]) {
              let logoFileBodyFormData = new FormData();
              logoFileBodyFormData.append("file", logoFile.files[0]);
              axios({
                method: "post",
                url: url + "/api/services/icon",  //
                headers: {"Content-Type": "multipart/form-data"},
                data: logoFileBodyFormData,
              }).then((logoRes) => {
                if (logoRes.data !== "") {
                  vm.service.logo = logoRes.data;
                } else {
                  vm.service.logo = null;
                }
                if(vm.samls){
                  if(vm.metaDataAddress || vm.metaDataFile){
                    if(vm.metaDataFile){
                      let metadataFile = document.querySelector("#metadataFile");
                      if(!metadataFile.files[0]){
                        alert("لطفا قسمت های الزامی را پر کنید.");
                      }else{
                        let metadataFileBodyFormData = new FormData();
                        metadataFileBodyFormData.append("file", metadataFile.files[0]);
                        axios({
                          method: "post",
                          url: url + "/api/services/metadata",  //
                          headers: {"Content-Type": "multipart/form-data"},
                          data: metadataFileBodyFormData,
                        }).then((res) => {
                          if(res.data !== ""){
                            vm.service.metadataLocation = res.data;
                            axios({
                              method: "put",
                              url: url + "/api/service/" + id + "/saml", //
                              headers: {"Content-Type": "application/json"},
                              data: JSON.stringify({
                                name: vm.service.name,
                                serviceId: vm.service.serviceId,
                                extraInfo: vm.extraInfo,
                                metadataLocation: vm.service.metadataLocation,
                                multifactorPolicy: vm.multifactorPolicy,
                                description: vm.service.description,
                                logo: vm.service.logo,
                                informationUrl: vm.service.informationUrl,
                                privacyUrl: vm.service.privacyUrl,
                                logoutType: vm.service.logoutType,
                                logoutUrl: vm.service.logoutUrl,
                                accessStrategy: vm.accessStrategy,
                                contacts: vm.contacts,
                              }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                              location.reload();
                            });
                          }
                        });
                      }
                    }else if(vm.metaDataAddress){
                      if(document.getElementsByName("metadataLocation")[0].value !== ""){
                        vm.service.metadataLocation = document.getElementsByName("metadataLocation")[0].value;
                        axios({
                          method: "put",
                          url: url + "/api/service/" + id + "/saml", //
                          headers: {"Content-Type": "application/json"},
                          data: JSON.stringify({
                            name: vm.service.name,
                            serviceId: vm.service.serviceId,
                            extraInfo: vm.extraInfo,
                            metadataLocation: vm.service.metadataLocation,
                            multifactorPolicy: vm.multifactorPolicy,
                            description: vm.service.description,
                            logo: vm.service.logo,
                            informationUrl: vm.service.informationUrl,
                            privacyUrl: vm.service.privacyUrl,
                            logoutType: vm.service.logoutType,
                            logoutUrl: vm.service.logoutUrl,
                            accessStrategy: vm.accessStrategy,
                            contacts: vm.contacts,
                          }).replace(/\\\\/g, "\\")
                        }).then((res) => {
                          location.reload();
                        });
                      }else{
                        alert("لطفا قسمت های الزامی را پر کنید.");
                      }
                    }
                  }else{
                    alert("لطفا قسمت های الزامی را پر کنید.");
                  }
                }else if(vm.oauths){
                  let supportedGrantTypesArray = document.getElementsByName("supportedGrantTypes")[0].getAttribute("value").split(", ");
                  let supportedResponseTypesArray = document.getElementsByName("supportedResponseTypes")[0].getAttribute("value").split(", ");
                  vm.service.clientId = document.getElementsByName("clientId")[0].value;
                  vm.service.clientSecret = document.getElementsByName("clientSecret")[0].value;
                  vm.service.supportedGrantTypes = [ "java.util.HashSet", supportedGrantTypesArray ];
                  vm.service.supportedResponseTypes = [ "java.util.HashSet", supportedResponseTypesArray ];

                  if(vm.service.clientId !== "" && vm.service.clientSecret !== "" &&
                      supportedGrantTypesArray[0] !== "" && supportedResponseTypesArray[0] !== ""){
                    axios({
                      method: "put",
                      url: url + "/api/service/" + id + "/oauth", //
                      headers: {"Content-Type": "application/json"},
                      data: JSON.stringify({
                        name: vm.service.name,
                        serviceId: vm.service.serviceId,
                        clientId: vm.service.clientId,
                        clientSecret: vm.service.clientSecret,
                        supportedGrantTypes: vm.service.supportedGrantTypes,
                        supportedResponseTypes: vm.service.supportedResponseTypes,
                        extraInfo: vm.extraInfo,
                        multifactorPolicy: vm.multifactorPolicy,
                        description: vm.service.description,
                        logo: vm.service.logo,
                        informationUrl: vm.service.informationUrl,
                        privacyUrl: vm.service.privacyUrl,
                        logoutType: vm.service.logoutType,
                        logoutUrl: vm.service.logoutUrl,
                        accessStrategy: vm.accessStrategy,
                        contacts: vm.contacts,
                      }).replace(/\\\\/g, "\\")
                    }).then((res) => {
                      location.reload();
                    });
                  }else{
                    alert("لطفا قسمت های الزامی را پر کنید.");
                  }
                }else{
                  axios({
                    method: "put",
                    url: url + "/api/service/" + id + "/cas", //
                    headers: {"Content-Type": "application/json"},
                    data: JSON.stringify({
                      name: vm.service.name,
                      serviceId: vm.service.serviceId,
                      extraInfo: vm.extraInfo,
                      multifactorPolicy: vm.multifactorPolicy,
                      description: vm.service.description,
                      logo: vm.service.logo,
                      informationUrl: vm.service.informationUrl,
                      privacyUrl: vm.service.privacyUrl,
                      logoutType: vm.service.logoutType,
                      logoutUrl: vm.service.logoutUrl,
                      accessStrategy: vm.accessStrategy,
                      contacts: vm.contacts,
                    }).replace(/\\\\/g, "\\")
                  }).then((res) => {
                    location.reload();
                  });
                }
              });
            }else {
              if(document.getElementsByName("logo")[0].value !== ""){
                vm.service.logo = document.getElementsByName("logo")[0].value;
              }else{
                vm.service.logo = null;
              }
              if(vm.samls){
                if(vm.metaDataAddress || vm.metaDataFile){
                  if(vm.metaDataFile){
                    let metadataFile = document.querySelector("#metadataFile");
                    if(!metadataFile.files[0]){
                      alert("لطفا قسمت های الزامی را پر کنید.");
                    }else{
                      let metadataFileBodyFormData = new FormData();
                      metadataFileBodyFormData.append("file", metadataFile.files[0]);
                      axios({
                        method: "post",
                        url: url + "/api/services/metadata",  //
                        headers: {"Content-Type": "multipart/form-data"},
                        data: metadataFileBodyFormData,
                      }).then((res) => {
                        if(res.data !== ""){
                          vm.service.metadataLocation = res.data;
                          axios({
                            method: "put",
                            url: url + "/api/service/" + id + "/saml", //
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                              name: vm.service.name,
                              serviceId: vm.service.serviceId,
                              extraInfo: vm.extraInfo,
                              metadataLocation: vm.service.metadataLocation,
                              multifactorPolicy: vm.multifactorPolicy,
                              description: vm.service.description,
                              logo: vm.service.logo,
                              informationUrl: vm.service.informationUrl,
                              privacyUrl: vm.service.privacyUrl,
                              logoutType: vm.service.logoutType,
                              logoutUrl: vm.service.logoutUrl,
                              accessStrategy: vm.accessStrategy,
                              contacts: vm.contacts,
                            }).replace(/\\\\/g, "\\")
                          }).then((res) => {
                            location.reload();
                          });
                        }
                      });
                    }
                  }else if(vm.metaDataAddress){
                    if(document.getElementsByName("metadataLocation")[0].value !== ""){
                      vm.service.metadataLocation = document.getElementsByName("metadataLocation")[0].value;
                      axios({
                        method: "put",
                        url: url + "/api/service/" + id + "/saml", //
                        headers: {"Content-Type": "application/json"},
                        data: JSON.stringify({
                          name: vm.service.name,
                          serviceId: vm.service.serviceId,
                          extraInfo: vm.extraInfo,
                          metadataLocation: vm.service.metadataLocation,
                          multifactorPolicy: vm.multifactorPolicy,
                          description: vm.service.description,
                          logo: vm.service.logo,
                          informationUrl: vm.service.informationUrl,
                          privacyUrl: vm.service.privacyUrl,
                          logoutType: vm.service.logoutType,
                          logoutUrl: vm.service.logoutUrl,
                          accessStrategy: vm.accessStrategy,
                          contacts: vm.contacts,
                        }).replace(/\\\\/g, "\\")
                      }).then((res) => {
                        location.reload();
                      });
                    }else{
                      alert("لطفا قسمت های الزامی را پر کنید.");
                    }
                  }
                }else{
                  alert("لطفا قسمت های الزامی را پر کنید.");
                }
              }else if(vm.oauths){
                let supportedGrantTypesArray = document.getElementsByName("supportedGrantTypes")[0].getAttribute("value").split(", ");
                let supportedResponseTypesArray = document.getElementsByName("supportedResponseTypes")[0].getAttribute("value").split(", ");
                vm.service.clientId = document.getElementsByName("clientId")[0].value;
                vm.service.clientSecret = document.getElementsByName("clientSecret")[0].value;
                vm.service.supportedGrantTypes = [ "java.util.HashSet", supportedGrantTypesArray ];
                vm.service.supportedResponseTypes = [ "java.util.HashSet", supportedResponseTypesArray ];

                if(vm.service.clientId !== "" && vm.service.clientSecret !== "" &&
                    supportedGrantTypesArray[0] !== "" && supportedResponseTypesArray[0] !== ""){
                  axios({
                    method: "put",
                    url: url + "/api/service/" + id + "/oauth", //
                    headers: {"Content-Type": "application/json"},
                    data: JSON.stringify({
                      name: vm.service.name,
                      serviceId: vm.service.serviceId,
                      clientId: vm.service.clientId,
                      clientSecret: vm.service.clientSecret,
                      supportedGrantTypes: vm.service.supportedGrantTypes,
                      supportedResponseTypes: vm.service.supportedResponseTypes,
                      extraInfo: vm.extraInfo,
                      multifactorPolicy: vm.multifactorPolicy,
                      description: vm.service.description,
                      logo: vm.service.logo,
                      informationUrl: vm.service.informationUrl,
                      privacyUrl: vm.service.privacyUrl,
                      logoutType: vm.service.logoutType,
                      logoutUrl: vm.service.logoutUrl,
                      accessStrategy: vm.accessStrategy,
                      contacts: vm.contacts,
                    }).replace(/\\\\/g, "\\")
                  }).then((res) => {
                    location.reload();
                  });
                }else{
                  alert("لطفا قسمت های الزامی را پر کنید.");
                }
              }else{
                axios({
                  method: "put",
                  url: url + "/api/service/" + id + "/cas", //
                  headers: {"Content-Type": "application/json"},
                  data: JSON.stringify({
                    name: vm.service.name,
                    serviceId: vm.service.serviceId,
                    extraInfo: vm.extraInfo,
                    multifactorPolicy: vm.multifactorPolicy,
                    description: vm.service.description,
                    logo: vm.service.logo,
                    informationUrl: vm.service.informationUrl,
                    privacyUrl: vm.service.privacyUrl,
                    logoutType: vm.service.logoutType,
                    logoutUrl: vm.service.logoutUrl,
                    accessStrategy: vm.accessStrategy,
                    contacts: vm.contacts,
                  }).replace(/\\\\/g, "\\")
                }).then((res) => {
                  location.reload();
                });
              }
            }
          }
        },
        deleteService: function (id) {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          let selectedServices = [];
          selectedServices.push(id.toString());
          var check = confirm(this.s87);
          if (check == true) {
            axios({
              method: 'delete',
              url: url + "/api/services", //
              headers: {'Content-Type': 'application/json'},
              data: JSON.stringify({
                names: selectedServices
              }).replace(/\\\\/g, "\\")
            })
            .then((res) => {
              vm.refreshServices();
            });
          }
        },
        deleteAllServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          var check = confirm(this.s88);
          if (check == true) {
            axios({
              method: 'delete',
              url: url + "/api/services", //
              headers: {'Content-Type': 'application/json'},
              data: JSON.stringify({
              }).replace(/\\\\/g, "\\")
            })
            .then((res) => {
              vm.refreshServices();
            });
          }
        },
        getGroups: function () {
          let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port; //
          let vm = this;
          axios.get(url + "/api/groups")
          .then((res) => {
            vm.groups = res.data;
          });
        },
        addGroup: function (n) {
          if(this.groupList.includes(n)){
            if(this.groupList.includes(n + ',')){
              this.groupList = this.groupList.replace(n + ',', "");
            }else if(this.groupList === n){
              this.groupList = this.groupList.replace(n, "");
            }else{
              this.groupList = this.groupList.replace(',' + n, "");
            }
          }else{
            if(this.groupList === ""){
              this.groupList += n;
            }else{
              this.groupList += ',' + n;
            }
          }
        },
        allGroups: function () {
          if(this.allGroupsHolderText == this.addAllGroupsText){
            for(let i = 0; i < this.groups.length; ++i){
              if(!this.groupList.includes(this.groups[i].id)){
                document.getElementById("groupNameId" + this.groups[i].id).click();
              }
            }
            this.allGroupsHolderText = this.removeAllGroupsText;
          }else{
            for(let i = 0; i < this.groups.length; ++i){
              if(this.groupList.includes(this.groups[i].id)){
                document.getElementById("groupNameId" + this.groups[i].id).click();
              }
            }
            this.allGroupsHolderText = this.addAllGroupsText;
          }
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
        EnNumToFaNum: function (str) {
          let s = str.split("");
          let sFa = "";
          for(let i = 0; i < s.length; ++i){
            if(s[i] === "۰" || s[i] === "0"){
              sFa = sFa + "۰";
            }else if(s[i] === "۱" || s[i] === "1"){
              sFa = sFa + "۱";
            }else if(s[i] === "۲" || s[i] === "2"){
              sFa = sFa + "۲";
            }else if(s[i] === "۳" || s[i] === "3"){
              sFa = sFa + "۳";
            }else if(s[i] === "۴" || s[i] === "4"){
              sFa = sFa + "۴";
            }else if(s[i] === "۵" || s[i] === "5"){
              sFa = sFa + "۵";
            }else if(s[i] === "۶" || s[i] === "6"){
              sFa = sFa + "۶";
            }else if(s[i] === "۷" || s[i] === "7"){
              sFa = sFa + "۷";
            }else if(s[i] === "۸" || s[i] === "8"){
              sFa = sFa + "۸";
            }else if(s[i] === "۹" || s[i] === "9"){
              sFa = sFa + "۹";
            }
          }
          return sFa;
        },
        allowUserAccess: function (user) {
          if(this.userAllowedId.indexOf(user.userId) == -1){
            this.userAllowedId.push(user.userId);
            this.userAllowed.push(user);
          }else{
            alert("کاربر پیش از این به لیست اضافه شده است.")
          }
        },
        blockUserAccess: function (user) {
          if(this.userBlockedId.indexOf(user.userId) == -1){
            this.userBlockedId.push(user.userId);
            this.userBlocked.push(user);
          }else{
            alert("کاربر پیش از این به لیست اضافه شده است.")
          }
        },
        deleteFromAllowedList: function (user) {
          let index = this.userAllowed.indexOf(user);
          if (index > -1) {
            this.userAllowed.splice(index, 1);
          }
          index = this.userAllowedId.indexOf(user.userId);
          if (index > -1) {
            this.userAllowedId.splice(index, 1);
          }
        },
        deleteFromBlockedList: function (user) {
          let index = this.userBlocked.indexOf(user);
          if (index > -1) {
            this.userBlocked.splice(index, 1);
          }
          index = this.userBlockedId.indexOf(user.userId);
          if (index > -1) {
            this.userBlockedId.splice(index, 1);
          }
        },
        addAttribute: function () {
          this.indexList.push(this.indexList[this.indexList.length-1] + 1);
          this.flagList.push(true);
        },
        deleteAttribute: function (index) {
          this.flagList[index] = false;
          document.getElementById("attribute" + index).remove();
        },
        changeSelected: function (action) {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          let selectedServices = [];
          if(action == "delete"){
              selectedServices = [];
              for(let i = 0; i < vm.services.length; ++i){
                  if(document.getElementById("checkbox-" + vm.services[i]._id).checked){
                    selectedServices.push(vm.services[i]._id.toString());
                  }
              }
              if(selectedServices.length != 0){
                  var check = confirm(this.s85);
                  if (check == true) {
                      axios({
                          method: 'delete',
                          url: url + "/api/services", //
                          headers: {'Content-Type': 'application/json'},
                          data: JSON.stringify({
                              names: selectedServices
                          }).replace(/\\\\/g, "\\")
                      })
                      .then((res) => {
                        vm.refreshServices();
                      });
                  }
              }else{
                  alert(this.s86);
              }
          }
        },
        rowSelected:function(id) {
            let row = document.getElementById("row-" + id);
            if(row.style.background == ""){
                row.style.background = "#c2dbff";
            }else{
                row.style.background = "";
            }
            this.allIsSelected = false;
            if(document.getElementById("selectAllCheckbox").checked == true){
                document.getElementById("selectAllCheckbox").click();
            }
        },
        persianInputCharCheck ($event) {
          if($event.type == "keypress"){
            let key = ($event.key ? $event.key : $event.which);
            let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
            if(keyCode < 48 || 57 < keyCode){
              if(32 < keyCode && keyCode < 65){
                $event.preventDefault();
              }else if(90 < keyCode && keyCode < 97){
                $event.preventDefault();
              }else if(122 < keyCode && keyCode < 127){
                $event.preventDefault();
              }else if(!persianRex.text.test(key)){
                $event.preventDefault();
              }
            }
          }else if ($event.type == "paste"){
            let text = $event.clipboardData.getData("text");
            for(let i = 0; i < text.length; ++i){
              if(text[i].charCodeAt(0) < 48 || 57 < text[i].charCodeAt(0)){
                if(32 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 65){
                  $event.preventDefault();
                  break;
                }else if(90 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 97){
                  $event.preventDefault();
                  break;
                }else if(122 < text[i].charCodeAt(0) && text[i].charCodeAt(0) < 127){
                  $event.preventDefault();
                  break;
                }else if(!persianRex.text.test(text[i])){
                  $event.preventDefault();
                  break;
                }
              }
            }
          }
        },
        englishInputCharCheck ($event) {
          if($event.type == "keypress"){
            let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
            if (keyCode < 48 || 122 < keyCode) {
              $event.preventDefault();
            }else if (57 < keyCode  && keyCode < 65) {
              $event.preventDefault();
            }else if (90 < keyCode  && keyCode < 97) {
              $event.preventDefault();
            }
          }else if ($event.type == "paste"){
            let text = $event.clipboardData.getData("text");
            for(let i = 0; i < text.length; ++i){
              if (text[i].charCodeAt(0) < 48 || 122 < text[i].charCodeAt(0)) {
                $event.preventDefault();
                break;
              }else if (57 < text[i].charCodeAt(0)  && text[i].charCodeAt(0) < 65) {
                $event.preventDefault();
                break;
              }else if (90 < text[i].charCodeAt(0)  && text[i].charCodeAt(0) < 97) {
                $event.preventDefault();
                break;
              }
            }
          }
        },
        showReport: function (id) {
          this.showS = "display: none;";
          this.editS = "display: none;";
          this.reportS = "";
          let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          let vm = this;
          let n = 1, m = 1;

          this.usersReportLoader = true;
          this.groupsReportLoader = true;
          axios.get(url + "/api/services/" + id) //
            .then((res1) => {
              vm.reportedService = res1.data;
              axios.get(url + "/api/groups")
                .then((res2) => {
                  vm.groups = res2.data;
                  for(let i = 0; i < vm.groups.length; ++i){
                    if(typeof vm.reportedService.accessStrategy.requiredAttributes.ou !== "undefined") {
                      if(vm.reportedService.accessStrategy.requiredAttributes.ou[1].length !== 0){
                        if(vm.reportedService.data.accessStrategy.requiredAttributes.ou[1].includes(vm.groups[i].id)){
                          vm.groups[i].recordNumber = n;
                          vm.allowedGroupsReportList.push(vm.groups[i]);
                          n += 1;
                        } else {
                          vm.groups[i].recordNumber = m;
                          vm.bannedGroupsReportList.push(vm.groups[i]);
                          m += 1;
                        }
                      } else {
                        for(let j = 0; j < vm.groups.length; ++j){
                          vm.groups[j].recordNumber = j + 1;
                        }
                        vm.allowedGroupsReportList = vm.groups;
                        break;
                      }
                    } else {
                      for(let j = 0; j < vm.groups.length; ++j){
                        vm.groups[j].recordNumber = j + 1;
                      }
                      vm.allowedGroupsReportList = vm.groups;
                      break;
                    }
                  }
                  vm.groupsReportLoader = false;
                  axios.get(url + "/api/users")
                    .then((res3) => {
                      vm.userSearch = res3.data;
                      n = 1; m = 1;
                      for(let i = 0; i < vm.userSearch.length; ++i){
                        if(typeof vm.reportedService.accessStrategy.requiredAttributes.uid !== "undefined"){
                          if(vm.reportedService.accessStrategy.requiredAttributes.uid[1].includes(vm.userSearch[i].userId)){
                            vm.userSearch[i].recordNumber = n;
                            vm.allowedUsersReportList.push(vm.userSearch[i]);
                            n += 1;
                            continue;
                          }
                        }
                        if(typeof vm.reportedService.accessStrategy.rejectedAttributes !== "undefined"){
                          if(typeof vm.reportedService.accessStrategy.rejectedAttributes.uid !== "undefined"){
                            if(vm.reportedService.accessStrategy.rejectedAttributes.uid[1].includes(vm.userSearch[i].userId)){
                              vm.userSearch[i].recordNumber = m;
                              vm.bannedUsersReportList.push(vm.userSearch[i]);
                              m += 1;
                            }
                          }
                        }
                      }
                      vm.usersReportLoader = false;
                    });
                });
            });
        },
        s2ab: function (s) {
          let buf = new ArrayBuffer(s.length);
          let view = new Uint8Array(buf);
          for (let i = 0; i < s.length; ++i) {
            view[i] = s.charCodeAt(i) & 0xFF;
          }
          return buf;
        },
        exportReports: function (format) {
          if(this.usersReportLoader || this.groupsReportLoader){
            alert("در حال بارگذاری داده ها");
          }else{
            let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
            let vm = this;
            this.exportReportLoader = true;
            if(format === "pdf"){
              let doc = new jspdf.jsPDF();
              doc.addFileToVFS("IRANSansWeb.ttf","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AAcAQAOZAVb//wCV/OkGugQFAiYACgAAAAcAQAN8ATQAAf9c/fcAo/8+AAMAABMHJzejo6Sj/pqjpKMAAAL+kP4VAXD/XQADAAcAAAMHJzcFByc3KqKkogI+oqSi/rmio6Olo6SiAAAAAQCgAAABjwWnABIAAAEWEhcWFRQGByc2NzY1NCcKAScBZBEUAwMFDsUOBQMCBxYOBafJ/pqhpoVQ+WMLnJlVVUNEAQcBiocAAQDMAAAClAWkAB0AACEjIi4CNTQ3NTQnLgEnNx4BFxYVFAYHFB4COwEClBR3nFslAggEDgnECQ4EDAECHTVJLRg4aJNct7wlqKJOkTAkM5RNvLYO0sQ8RyYNAP////0AAAICB/4AJgB7AAAABwBrAP0ABf//ACUAAAKUB/4CJgB8AAAABwBrASUABf//AAP9NwIIBacCJgB7AAAABwBsAPgAAP//AGf9NwKUBaQCJgB8AAAABwBsAVwAAP///+IAAALcBwcAJgB7WgAABwBqAUEAAP///+IAAALcBwcCJgB8AAAABwBqAUEAAP///7sAAAJHB2UAJgB7HgAABwBxAUIAAP//AAcAAAKUB2UCJgB8AAAABwBxAY4AAP//AGH99weGA38CJgA1AAAABwB5A94AAP///+z99wJvA5wCJgByAAAABwB5AS8AAP///+z99wLVAuQCJgBzAAAABwB5AVAAAP//AKn99wkgA4oCJgA2AAAABwB5BCIAAP//AGH81weGA38CJgA1AAAABwBAA+QAAP//AKn81wkgA4oCJgA2AAAABwBAA+0AAP///+z81wL5A5wCJgB0AAAABwBAAX8AAP///+z81wNoAuQCJgB1AAAABwBAAX8AAP//AGEAAAeGBHsCJgA1AAAABwA+A/P+l////+wAAAMuBZUCJgB0AAAABwA+Ab7/sf///+wAAANoBPkCJgB1AAAABwA+Ab7/Ff//AKn//QkgBHsCJgA2AAAABwA+BFf+l///AGEAAAeGBYsCJgA1AAAABwA/A/T+kv///+wAAAMbBrcCJgB0AAAABwA/AaL/vv///+wAAANoBhcCJgB1AAAABwA/AaL/Hv//AKn//QkgBYsCJgA2AAAABwA/BFb+kv//AGEAAAeGBc4CJgA1AAAABwBvBDr+fv///+wAAAMGBvICJgB0AAAABwBvAeT/ov///+wAAANoBlUCJgB1AAAABwBvAeT/Bf//AKn//QkgBc4CJgA2AAAABwBvBKX+fv//AE8AAAN9BeQCJgAqAAAABwA+AdYAAP//AIUAAARWBeQCJgAtAAAABwA+AlcAAP///+z81wYfA5QCJgAIAAAABwBAAycAAP///+z82QaXA5QCJgAJAAAABwBAAuMAAv//AF786QYRBBYCJgAHAAAABwA9A5X6a////+z9+QYfA5QCJgAIAAAABwB5AyUAAv///+z99waXA5QCJgAJAAAABwB5AyUAAP//AJX86Qa6BAUCJgAKAAAABwA9A6T6SP//AF786QYRBjQCJgAHAAAABwA9Am4AS////+wAAAYfBZQCJgAIAAAABwA9AnP/q////+wAAAaXBZgCJgAJAAAABwA9AiP/r///AJX86Qa6BhECJgAKAAAABwA9AoEAKP//AGcAAQQIBnACJgALAAAABwA9Ac4Ah///AH8AAAUCBnQCJgBwAAAABwA9Am8Ai///AGcAAQQIB64AJgALAAAABwBvAksAXv//AH8AAAUCB8gCJgBwAAAABwBvAvYAeP///3/9NAJrBMgCJgAMAAAABwA9AYv+3////4b9HgNBBMgCJgANAAAABwA9AYv+3////3/9NAMBBdwAJgAMAAAABwBvAd/+jP///4b9HgNBBdwAJgANAAAABwBvAd/+jP///3/9NAMCBZ8AJgAMAAAABwA/AYn+pv///4b9HgNBBZ8AJgANAAAABwA/AY3+pv//AGv9bQoXBlsCJgAOAAAABwA/B4r/Yv///+z/9AYKBlsCJgAPAAAABwA/A5b/Yv///+z//QdPBf4CJgAQAAAABwA/A+b/Bf//AGz9aQtdBf4CJgARAAAABwA/B8j/Bf//AKf9dwr8BYwCJgASAAAABwA9CVb/o////+z/7AcUBYwCJgATAAAABwA9BYX/o////+z/7AehBYwCJgAUAAAABwA9BXf/o///AIn9dwtIBYwCJgAVAAAABwA9CTL/o///AFsAAAaqBiECJgAWAAAABwA9BSgAN////+wAAAWpBiACJgAXAAAABwA9BEAAN////+wAAAZDBiACJgAYAAAABwA9BEAAN///AFsAAAdfBiECJgAZAAAABwA9BSkAN///AGf9PwWaB1MCJgAaAAAABwA9A5ABav///+wAAAP0BcMCJgAbAAAABwA9AlD/2v///+wAAASMBcICJgAcAAAABwA9Amv/2f//AF/85wVhBh0CJgAdAAAABwA9Az8ANP///+wAAAP9BmACBgAeAAD////sAAAEuAaEAgYAHwAA//8AaQABB10GawImADkAAAAHAD0FzgCC////7AAAA0oGawImADcAAAAHAD0BpACC////7AAAA/0F1QImADgAAAAHAD0B7f/s//8AYP/qCE4F6QImADoAAAAHAD0GWwAA//8AaQABB10HcQAmADkAAAAHAD8FzQB4////7AAAA0oHdwAmADcAAAAHAD8BqgB+////7AAAA/0GuQImADgAAAAHAD8B9v/A//8AYP/qCE4GuQImADoAAAAHAD8GVP/A////7AAAA0oGUQImADcAAAAHAD4BpgBt////7AAAA/0FqQImADgAAAAHAD4B7v/F//8AVv2BBcEFYQImADsAAAAHAD4D9v99//8AVv2BBjYFYQImADwAAAAHAD4ED/99////7AAAAm8FigImAHIAAAAHAD0BK/+h////7AAAAtUE4QImAHMAAAAHAD0BKv74//8Aa/1RBe8DKgImAFkAAAAHAD0DNP1B//8AOv1SBsMDKgImAFoAAAAHAD0DAP1B//8ATwAAA30EIQIGACoAAP//AE8AAAN9BpsAJgAqAAAABwBrAaP+ov//AIUAAARWBpECJgAtAAAABwBrAiv+mP//AE39QgPIBdMAJgAuAAAABwBrAcz92v//AE79QgQnBdMCJgAvAAAABwBrAcz92v///+wAAAJvA5wABgByAAD////sAAAC1QLkAgYAcwAA////7P4VAuIDnAAmAHQAAAAHAHoBcAAA////7P4VA2gC5AImAHUAAAAHAHoBfgAA//8AX/uFBtADSwImADAAAAAHAHoDLf1w//8AYvueBscCjAImADEAAAAHAHoDHv2J//8AX/1BBtADSwIGADAAAP///+z+FQLiA5wCJgB0AAAABwB6AXIAAP///+z+FQNoAuQCJgB1AAAABwB6AXIAAP//AGL9dAbHAowCBgAxAAD//wBS/mEFEgNZAiYAUQAAAAcAawFv+2D//wBS/mEFWANBAiYAUgAAAAcAawGy+0j//wBf/UEG0AT/ACYAMAAAAAcAawJt/Qb////sAAAC4gYlACYAdAAAAAcAawGn/iz////sAAADaAV6AiYAdQAAAAcAawGJ/YH//wBi/XQGxwU8AiYAMQAAAAcAawKE/UP////Q/7EEmwfSACYAMgAAAAcAawDQ/9n////Q/7YFiwfSAiYAMwAAAAcAawDQ/9n//wBN/Q8EmwX2ACYAMgAAAAcAbAGB/9j//wBc/Q8FiwX7AiYAMwAAAAcAbAGB/9j///91/7EEmwaYACYAMgAAAAcAagDU/5H///91/7YFiwaYAiYAMwAAAAcAagDU/5H////d/7EEmwbpACYAMgAAAAcAcQFk/4T////d/7YFiwbpAiYAMwAAAAcAcQFk/4T//wCFAAAEVgRHAgYALQAA//8ATwAAA30GrQAmACoAAAAHAGsBnf60//8AYAAABVcE4AImAHYAAAAHAGsBdPzn//8ATwAAA30EIQAGACoAAP//AE8AAAN9BagAJgAqAAAABwA+AdT/xP//AGAAAAVXBGwCJgB2AAAABwA+AlH+iAACACT9bgSoA5IAFQBeAAABDgEHDgMHBhUUFz4CNzU0Jy4BATQ+Aj8BJjU0Njc+Azc+AzczMh4CFxYXFhUUDgIHDgEHBgcWFz4FOwEVIyIOAgcVFBYXBxcHJgIvAQciJgLqEC8aDyAeGggGAmF9PgIYDh/9LQsXJRvTAxAYESosLBMPKzQ5HgUfOzEjCUAUBwgrSjQqXTkgJC5bCCo/UV5pNxQcNllAJQIbIwEZfb7zNwn4KjgCwQwuIxU2Q04tKSwaHBJddkEFPTUeK/1YFBwWEgtUIiIteUwyVUY4FREqJhoCHCkqC1puKSsVWH5vKB8uDgoGdHo0Z15QOiDELk5kNRItYi0CFZiaATinGVQ4////7P/9BYYEXAIGACsAAP///+z9bgQfA5ICBgAsAAD//wCqAMsDUQNyAgYAQQAA//8Aef/mAiIFoAIGAEIAAP//AET/7APkBbYCBgBDAAD//wAy/+0FNwXBAgYARAAA//8Abv/OA8UGHAIGAEoAAP//AFn/6QTDBcMCBgBIAAD//wCO/+AE+AWqAgYASQAAAAEApP/2BG0FkABAAAABHgMXBgcOAw8BJicmJyYjIgcOARUUHgIfAR4DFwcuAScuAScuAicmNTQ+ATc+AhYXHgEXPgMEHgUUFhcJRT4bOjgyE3UjTygoISEJCCUeDBstIVgkNSQTAZkPOC0rTysUJRcCARQ3MRROXGAnKD8cEUZXXwWQDCMmJA88SCBMV2I1JadyOSEaAgY4HxlCV21EtFCVg20nKmHEamSzXSpjaTULCylWUx4NFgEeJyhyRi5rZVMAAAABAEgAAgN3BUMALAAAJQ4DIyIuAicuATU0Njc+Azc+AzceAxcHAw4DFR4BMzI2NwN3P2dodEseXGFYGgoLRj0eT2aDUwwiJiYQCxocHAxz8E5rQx1CkD9kvFs3EhULAwYKDQcqVi1gw2czepCnYQ4pLSwRDB8hIQ6N/shmrIdiHBEPFw4AAAD///8HBjcA/QhUAgYAYgAA///+ugXcAUYIHgIGAGYAAP///v78zQD0/uoCBgBjAAD///8DBjQA+QdOAgYAZAAA///+/QXoANkIOAIGAGUAAP///wX9zQD7/ucCBgBnAAD///7tBpQBEwgVAgYAaAAA////JgaFANwIPAIGAGkAAAAC/rwEeQEXBzUACwA+AAADNjU0Jic3HgIGBwUmNTQ+AjcXDgIHBhUUFx4DOwEyNjc2NTQuAiM3HgIXFhUUBw4DIyIuAj0NAgtKBgcBBwr+tgQGFSASRAgYFAUCAgMVLUk2BVhtFA0IHR0BRgIlIwYBEhA6Tl80N19KMQX+SkIFS0kSHk1XWyqaGhoRPE9HHTEMNEUmExMTExEyLiFDPCkoD0VYNywDQ2lDDg41NzBGLhYbNlIAAAD///66BpQBRgo5AiYAaAAAAAcAZgAAAhv///7tBF4BEwgVAiYAaAAAAAcAYwAJB5H///7tBpQBEwo1AiYAaAAAAAcAYv/9AeH///7tBpQBEwlOAiYAaAAAAAcAZAAEAgD///7tBpQBEwpbAiYAaAAAAAcAZQAAAiP///7tBTwBEwgVAiYAaAAAAAcAZ///B2////7tBpQBEwoqAiYAaAAAAAcAbQAAAgr///8ABagBBQorAicAYgAAAdcABgBrAAD///8ABagBBQkpAicAZAAAAdsABgBrAAD///66BagBRgp6AicAZgAAAlwABgBrAAD///8ABagBBQp8AicAZQARAkQABgBrAAD///8ABagBBQoHAicAaQAAAcsABgBrAAD///8E+0IBEP+IAicAYwAG/nUABgBsAAD///8F/EEBEP+IAicAZwAA/nQABgBsAAAABAB5//oHpwfrAFsAbwC9AMkAAAUVIicjNSImJyYnLgEnBiMgJy4BJzU0Nj8BPgM3LgE1NxoBFxYXFjMyNz4BNzY1NAM3GgEVFBYXFhczMjY3PgE1NAM3Eh0BBgcGKwEGIyInJicmJw4BBw4BIwEOAwcGBxUUFx4BFxYzMjcuAQEiJyYnJj0BPgE3FwcOAQcGFRQWFx4BMzI+Ajc+ATc+ATcXFBYXHgEXHgMzMjc+ATU0LgInNx4DFRQHDgMjIicmJw4DEzY1NCYnNxYXFRQHBCohIgwFCQUzIShBGn1f/v9lHBkBCgkBKX2GfioHCcQcNBcuSiUdHRYtPA8EKsYfHwsaGSsUMlASEglLwkUCQj+1ExAQIyMUEkUoCxMKOXs0/iQkV1VMGQIBCwctMTE/P04LGwIZNSITCQcBCQFOAwIBAQEJCQMJCw0YFA8FBwgDAgIBUwEBAgcGBAoPEw0RCgUPDBISBkYBFRgULgwZFxIFOSoRCg8iIyM4EwESSRADFAMDAwEBAQcYEzwqEn0hSiMRGjMTBVWAXDsQS18CEf67/n09egIBAQMvIggzrAKXFf5S/k4EEjgeHQISGhY6HQkDfSz8bF0CU2JdAwwCBxwlCBAHJBQCuxAvQFEyBxEEDw0IGwgHCTqpAvAnGiUeKhMjMgIRFgsbDgoJFysNAwYQGR8PFCYPCREIAwgQCQ8lFA4dGBAHByUrFjItJQooAiU6RyRiLgwNBgEzFBsfJxcIAY1FRwNOQhc8Uw5OYQD//wBq/TQMvAX5ACIAIgAAACMAcgfgAAAAIwAMClEAAAAjAAYFWwAAAAMAeggjAAAAAQBS/rwB9wW2AA8AABMQEjcXBgcGFRQXFhcHJgJSmpN4V0pISEpSc5SZAjEBCgHMr09w6ef07efnZFirAcMAAAEAZf68AhcFtgAPAAABEAIHJzY3NjU0JyYnNxYSAhebk4RhSkpISGB/kpwCMf72/j6pPnnl5/T05+Z9Ra7+NgABAF786QYRBBYASQAAATI2NxcGBCMiJC4BNTQ+AyQ3LgMnLgMnIg4CBy4DJz4DNx4BFx4DFw4DByImJyMiDgQHFRQeAgNkj/x8cpX+tL6a/vTHc1OUzPEBDowgPTs4GS1LS1I0KVZRRRgYKCYnFSRccotTdttqT4N1bDgJEBAQCSkxGhli5OvXp2gHXaHR/bVUWKFncFSg6pV0zKiFXDABDiEiIQ0XKyMWASM5RyUPGhkZDjxmTS8GAlFFNE85JwwiOTc4IQIBH0JifplYCW2lbzYAAAEAlfzpBroEBQBeAAABIi4DJyY1NDc+Azc2JDcnLgEnJiMiBw4BByc+Azc2NzYzMhceARceARcHIi4BBgcUHgI7ARUjIi4CNQ4BBw4FBwYVFB4DFzMyPgE3Fw4DA5Btya+NYBYRAwlKdp5dfAEEf2xprjoTE3VgISoNuAISHy4fWGpMUyEjVMF5bfaRJgIhO1ExMl+IVxQUhdKSTnDlahdCSEg7KQUBNGqUq1wRUqukQoFHnKOl/OkwWX2ZWUBFGhxgs51/KzlADzQ1TQgDZiRFG0wEJDRBIWEoHQULVTw2cjDAAQECA1FlOhXEMWqndQ42MAslNUZYaT4QEE2QeVIsASVRP5g/UzEVAAABAGUFZgOJBx8AAwAACQE3AQOJ/NwHAu4Guv6sgAE5AAH/3AC3ACQGgwADAAATESMRJEgGg/o0BcwAAAAB/yUAtwDbBoMADgAANyMRByc3JzcXNxcHFwcnJEiGMaurMaqqMaurMYa3BKGIMamoMaurMaipMYgAAAAB/9wAtwGuBoMACgAAAQcnNyERIxEhJzcBrtkxif72RwFRiTEFrNYxgvsuBRmCMQAB/lEAtwAjBoMACgAANyMRIRcHJzcXByEjR/72iTHZ2TGJAVG3BNKCMdbXMYIAAAAF/+z9egbUAucAAwAHAAsADwBTAAABByc3BQcnNwUHJzcFByc3JSMiJicOAysBIiYnDgMrATUzMj4BNzY9AS4BJzceAxceAhcWOwEyPgE3Nj0BLgEnNx4DFx4CFxY7AQE3oqSiAiCipKICzKKkogIgoqSiASEbZYQqJHKJlkhUZYQqJHKJlkhZWVSadSIfAg8WuwkQDAkCAg4kIB0wW1SadSIfAg8WuwkQDAkCAg4kIB4wIf4eoqOjpaOkojyio6Olo6Si2kk3IzEeDkk3IzEeDsUUNi8qQQw4eE01I01QUSclUkYXFhQ2LypBDDh4TTUjTVBRJyVSRhcWAAAF/+z9egZYA5wAAwAHAAsADwBTAAABByc3BQcnNwEHJzcFByc3AToBPgM3NjU0JicuAScmJzcUHgIXFhUUBwYHDgMrASImJw4DKwE1MzI+ATc2PQEuASc3HgMXHgMzBNyipKICIKKkovuDoqSiAhaipKIB6xM9VFFENA0OCyALHQ4QEbITHCIPNR5HtRtUWFAWoWWEKiRyiZZIWVlUmnUiHwIPFrsJEAwJAgIOJD8z/qeio6Olo6Si/tWio6Olo6SiAgQIDxsoHB8xFGZqIEsgJiVYASdCVzGkel1FoTMICQQBSTcjMR4OxRQ2LypBDDh4TTUjTVBRJyVSRi4AAAAAAgCC//gBwAZlAAUACQAAEwMRMxEDEwcnN9cm5xpCo5uiAc0DaAEw/tD8mP7Oo5ukAAAAAAEAYP1sAyMHKwAUAAASExIlFh8CFhcGBwIDEAEHJAMCEWCepAEmCg0WFwwL9XeDBQH0Z/7on6UDzQFHAVTDEhQkJBUSzvv+7/6N/ZL+LZzdAUABTwF6AAABAFL9bAMVBysAFgAAJAMCBS8BJicAEQIDJic2PwE2NwQTEhEDFaae/ugaGR8VAfQFg3f1CwwXHRABI6Wg2P6x/sDdKCcwHQHTAm4BcwER+84SFSQwGsT+q/62/ooAAAABALT/gAG/AH4ADwAANjc2MzIXFhUUBwYjIicmNbQhI0BBJCIiJUBAIyE2IyUlIzo2IiQkITcAAAIAqP+AAbQDpwAPAB8AADY3NjMyFxYVFAcGIyInJjUSNzYzMhcWFRQHBiMiJyY1qCEjQEAlIiIlQEAjIQEhI0BBJCIiJUA/JCE2IyUlIzo2IiQkITcDYyMlJSM6NyEkJCE3AAACAJkEkgJmBr0ABAAJAAABAyMTMwUDIxMzATcifAGdAS8hfQGdBiT+bgIrmf5uAisAAAAAAgCGAAAFawZjABsAHwAAASEDIxMhNSETITUhEzMDIRMzAzMVIQMzFSEDIwMhEyEDW/7YWqFa/vQBKU7+3gFAXKFcAShcolzl/v1O/f7mWqKxAShO/tcBzP40AcyaAY6cAdP+LQHT/i2c/nKa/jQCZgGOAAABAHz/FgSRB2wAKwAAATQmJy4BNTQ2NzUzFRYSFSM0JiMiBhUUFgQeARUUBgcVIzUmAjUzFBYzMjYDwZGr8NvXu6i80s6XgIWOlgFWwFvkzafR7M+kl5OoAaVnjjlJ67W47Bb29xr+9+aeu4p8cohvhrJ3vegV19cTAQPfnKmOAAUAdv/oBjEGewANABsAKQA3ADsAABM0NjMyFh0BFAYjIiY1FxQWMzI2PQE0JiMiBhUBNDYzMhYdARQGIyImNRcUFjMyNj0BNCYjIgYVAScBF3a7lJW5u5GSv5tjU1BhYFNUYAKBvJOTvbySkr+bY1NRYWJSUGT9yXYDH3UFKZO/wJlQlLy8nAhXcm5gUldzc1z8bZO+vZxQk768nAdYcm9gU1lwb1/+8EoE/koAAAAAAwBx/+oFjwZ6AB4AJwAzAAATNDY3LgE1NDYzMhYVFAYPAQE2NTMQBxMjJw4BIyIkBTI3AQcGFRQWAxQXNz4BNTQmIyIGcYS5bUrcvajcZHx4AWxMu4rq+W1T4HTv/t4CEaWJ/nIlvKwmhIVMOHBVXGwBt3bGg4WhULrUxZVjp11Y/k2Ss/7hvP7ogUpN/VR+Ad0bipKEoARgbKJdNmJGS2R9AAABAHQEowEcBr0ABAAAAQMjEzMBHBeRAacGQP5jAhoAAAEAHwKsA74GYwAOAAABJTcFAzMDJRcFEwcLAScBc/6sNAFTCqwLAU00/qffjNHKjARQZap+AYL+eHyrZv7QagFD/sVmAAAAAAEAWACkBLgFSgALAAABIRUhESMRITUhETMC8AHI/jjQ/jgByNADbcT9+wIFxAHdAAEAIf66AVoA9gAIAAATJzY3NTMVFAaWdWkFy2/+ulGTnLyjceMAAAABACoCYgJOAwsAAwAAASE1IQJO/dwCJAJiqQAAAQAU/3QDcAZjAAMAABcjATPHswKrsYwG7wAAAAIAgf/qBIkGegANABsAAAEQAiEgAgMREBIhIBITJxAmIyIGBxEQFjMyNhMEifn+9/76+wX5AQsBCPkD0JSgoJICmpyalQMCuf6T/p4BWwFYARYBaAFf/q7+nw8BB/Lw+v60/vf97gEAAAAAAAEAvwAAAzMGawAGAAAhIxEFNSUzAzPR/l0CUyEFaZq93wAAAAEAaAAABLcGegAXAAApATUBPgE1NCYjIgYVIzQAMzIEFRQJASEEt/vRAjZ+X5uBm6zQASP05AEJ/sz+SgM1lQJ1j7JggKSwnuIBF/DG8v6z/iUAAAEAav/qBHYGegAnAAABMz4BNRAhIgYVIzQAMzIEFRQGBx4BFRQAIyIkNTMUFjMyNjU0JicjAback6n+4Yah0AEc2+gBBop3h5P+4ebn/uDQqY2Xn6+lnAOXApaAASCaf8IBAvXaa8gxK8WP3P7++cyBm52Tj5kCAAIAPAAABNgGYwAKAA4AAAEzFSMRIxEhNQEzASERBwP14+PR/RgC3N39MgH9GQIlqf6EAXx6BG37wgMiLQAAAAABAK3/6gSxBmMAHQAAGwEhFSEDNjMyABUUACMiJCczHgEzMjY1NCYjIg8B51MDRv1rMnmY4AEH/u/12f7uE8QUoYWRpbOUiE03AzQDL8D+Qkf+2fz9/t7w1IyPxq6kxzwtAAAAAgCU/+oEnQZkABQAIQAAARUjBgAHNjMyEhUUACMiABE1EAAlAyIGBxUUFjMyNjU0JgO3JvP+4ReC39b+/u3n6/7cAYEBfexqtCO2iIyhowZksAX+6v2V/tPv/P7RAWgBHFABwwHeBf0ggGFMy/fPp6nQAAABAFYAAASoBmMABgAACQEjASE1IQSo/VraAqP8iwRSBe76EgW4qwAAAwB+/+oEjgZ6ABcAIwAvAAABFAYHHgEVFAQjIiQ1NDY3LgE1NCQzMgQDNCYjIgYVFBYzMjYBIgYVFBYzMjY1NCYEaIJugJb+4ens/uSRgG1+AQnZ1wELqq6Mjamlk5Km/sh7l5V9fJabBLh6vzY304bU+/zThdU3Nr95z/Pz/DeJrauLi6ChBJuYgn2ZmX19nQAAAAIAcP//BHUGegAXACQAAAEOASMiLgE1NBI2MzIAERUQAAUjNTMkAAEyNjc1NAIjIgYVFBYDpEG1bI3Sc33lmfIBGP6H/oMoKwECART+zGmwKLGIiaagAs9OXov9maQBBoz+lf7HPf4j/kgFrwUBAwEZgF5TzQEA0qyo2AACAC7+ugF/BMoACwAUAAATNDYzMhYVFAYjIiYTJzY3NTMVFAZ9QEBAQkJAQEAndmoEy28ETTVISDU0Rkb6oVGTnLyjceMAAAABAFEA2wPoBNEABgAACQEVATUBFQEoAsD8aQOXAtP+5NwBqqQBqNwAAgCrAcAEUwRHAAMABwAAASE1IREhNSEEU/xYA6j8WAOoA5K1/Xm0AAEAlgDcBFYE0gAGAAAJATUBFQE1A339GQPA/EAC2gEj1f5YpP5W2AACAFT/9APjBnoAGAAkAAABPgE/ATY1NCYjIgYVIz4BMzIWFRQPAQYVAzQ2MzIWFRQGIyImAZECOFeTXnt2c4vQAv/N1O22elLZPj08Pz88PT4BzIabXphrenaGema24OTHxb95W6v+nDNERDIzQkIAAgCD/gMHogZHAEQAUwAAAQoBIyInDgEjIicmNTQ3NhI2MzIWFwMGFRQzMjY3NjUQACEiBAIDBhUUEgQzMjY3Fw4BIyAkJyYRNDcaAQAhIAQXFhEUBQYVFBYzMjY/ARMmIyICB6AO8svSPD2cU59SQQQRiNZ2W5BaOwKSf50HAv6k/oni/qLKDQKXAUvrZctDKkbmdv7n/mxkWQIN+QGrAQwBGAGQYlb7fgRPY0N9KAE0P0iDrAI0/vD+xrxfXYJnpysvugEipzFG/ZEdGsz8yi0rAXUBsuD+Wv7sJyXn/oLYMSeBLDj97tABDyUnATUB7QEM++3R/vAkhikkaJVzagoCNSH+9QAAAgAfAAAFvgZjAAcACgAAASEDIwEzASMBIQEERf1Tmt8Ccb0Ccd38+AIs/ukBq/5VBmP5nQJcAv0AAAAAAwC+AAAFFwZjAA4AFgAfAAAzESEgBBUUBgceARUUBCEBESEyNjUQISUhMjY1NCYjIb4CFgELAQyCcYWa/uL+/P6gAWSXrv6//pgBRo6pnaH+wQZj3NhysDEl0I/c/AL9/bOciQEorY16hnsAAQCG/+oFcQZ6ABwAAAECACEgABE1NBIkMyAAEyMuASMiAhEVEBIzMjY3BXEf/r7+9f7e/qOiASvFAQQBOxrZHLyoz+vfyLPAIAIH/vz+5wGfAVyd5AFauv7d/v/Dr/7P/uaf/vb+xqPKAAAAAgC+AAAFXAZjAAsAFQAAMxEhMgQSFxUUAgQHAxEzMgARNRAAJ74BzdYBSLIBsv613e3j+gEV/vrwBmO9/qThaef+pLsCBbL6/gE2AR9fARcBNAMAAQC+AAAEzQZjAAsAAAEhESEVIREhFSERIQRa/TsDOPvxBAP81ALFAvT9vLAGY7H98gAAAAEAvgAABLMGYwAJAAABIREjESEVIREhBET9UdcD9fziAq8C0v0uBmOx/dAAAQCJ/+oFdQZ6AB8AACUGBCMiJAInNRAAISAAFyMCISICAxUQEjMyNjcRITUhBXVT/tbFyP7KqgIBWQE4AQABOCPYPP662d8B+9d5tjz+hQJR13h1uwFa5I8BcQGX/vrwAUT+0P7fhf7s/rw2QAFvrwAAAAABAL4AAAWmBmMACwAAISMRIREjETMRIREzBabY/MfX1wM52AL0/QwGY/1BAr8AAAABAM4AAAGlBmMAAwAAISMRMwGl19cGYwABADz/6gREBmMADwAAATMRFAAjIiQ1MxQWMzI2NwNr2f7m6/T+8deakoalAQZj+3rq/vf54IydqJgAAAABAL4AAAWjBmMACwAAAQcRIxEzEQEhCQEhAl3I19cC1wEF/XwCtv7+AvjP/dcGY/zYAyj9LvxvAAEAvgAABJ0GYwAFAAAlIRUhETMBlwMG/CHZsLAGYwAAAQC+AAAHGQZjAA4AAAkCIREjERMBIwETESMRAdQCFwIWARjYFf3npf3oFdcGY/rKBTb5nQJ9Aq/61AUp/VT9gwZjAAABAL4AAAWmBmMACQAAISMBESMRMwERMwWm2PzJ2dkDOdYE7PsUBmP7DgTyAAIAhf/qBagGegARAB8AAAEUAgQjIiQCJzU0EiQzMgQSFScQAiMiAgMVEBIzMhITBaii/tfFwf7VpQKjASzCxAEsotfp0s3rA+3Q0eUDAv3x/pq8vQFg53bsAWrAvv6X7wIBIwE3/sn+7Hj+5v6+ATABHAAAAgC+AAAFVgZjAAoAEwAAAREjESEgABUUACElITI2NTQmJyEBldcCWwEMATH+1v7r/n4BhK24uKH+cAKA/YAGY/7u4u7+/7CjmpKvBAAAAAACAHr+7AWkBnoAFQAjAAABFAIHBQcBBiMiJAInNTQSJDMyBBIVJxACIyICAxUQEjMyEhMFn5eIASST/qdRWcL+1qUDpAEswcYBLKLY5tbL6wPr0dHlAwL97f6pYOWIARIUvQFg53bsAWrAv/6Z7wEBIQE5/sn+7Hj+5/69ATABHAAAAAACAL0AAAVgBmMADgAXAAABIREjESEgBBUUBgcBFSMBITI2NTQmJyEDFv5/2AIdARQBKqWTAYDo/R0BTKC+tar+tQKV/WsGY/vxmOM4/UkNA0ami5iiAQAAAQBa/+oE/gZ6ACcAAAEkJjU0JDMyBBYVIzQmIyIGFRQWBB4BFRQEISIkJjUzFBYzMjY1NCYCoP7q/AE096kBCJHZvayfs6oBmOZv/sr+/an+3J7Z27eqtqgC2VDoq8H9g+WIlamMfWSKcou4fMfvgeCPlKyLeHiEAAAAAAEANwAABSgGYwAHAAABIREjESE1IQUo/fLX/fQE8QWy+k4FsrEAAAABAJ3/6gU9BmMAEgAAAREGAA8BIAAnETMRFBYzMjY1EQU9Af7f9zn+8/7BAtbDtbfCBmP7qOf+2hICASH+BFr7rbLEw7IEVAABAB8AAAWaBmMABgAACQEzASMBMwLbAdPs/aK//aLrAR4FRfmdBmMAAQBFAAAHxwZjABIAAAEXNwEzARc3EzMBIwEnBwEjATMCHiAuAUO2ATwtI/3Z/nTF/q8aGf6ixP511wID18IEdfuLxt0EXvmdBKh8fPtYBmMAAAAAAQBAAAAFZQZjAAsAAAkBMwkBIQkBIwkBMwLTAYj+/fsCEf8A/m7+bP8CEv36/QPwAnP81fzIAn79ggM4AysAAAEAEQAABVAGYwAIAAAJATMBESMRATMCsAGr9f3M1/3M9wMuAzX7//2eAmIEAQAAAAEAYQAABQcGYwAJAAAlIRUhNQEhNSEVAWADp/taA4D8jgR0sLCiBRCxngAAAQCk/qICSwdNAAcAAAEjETMVIREhAkvW1v5ZAacGoviqqgirAAAAAAEALf90A50GYwADAAATMwEjLcYCqsUGY/kRAAABAAr+ogGzB00ABwAAEyERITUzESMKAan+V9nZB033VaoHVgABAEgDMwN1BmMABgAAAQMjATMBIwHe1cEBUI4BT8AFUP3jAzD80AAAAQAE/1YECQAAAAMAAAUhNSEECfv7BAWqqgAAAAEAQAVxAhQGuwADAAABIwEzAhSy/t76BXEBSgACAHr/6gRlBNYAHgAoAAAhJicGIyImNTQkITM1NCYjIgYVIzQ+ATMyFhcRFBcVJTI2NzUjIBUUFgOLEguRybPnASEBBsqCf3CW0YHehNLuBSr9zmGwJ6P+goIkXJbLnL7SX22BcFBbqmPSuf3Rp2MSnmVR+uBibgACAJ3/6gSiBr0ADwAaAAABEAAjIicHIxEzETYzMhIRJzQmIyIHERYzMjYEov8A2OZ+Cr/Qft3d/dCkms1aX8uVpgJS/ur+rqKMBr39fJ3+sf7dBtTnv/3zv+cAAAEAZ//qBGgE1gAdAAAlMjY3Mw4CIyIAETU0EjYzMgQXIy4BIyIGHQEUFgKFb6YJxQaF3Xv5/tuD86fMAQ8JxQmhdZ+urZOHZWi9cAFLAR8jsgEUmfXEdpjk2CfS4wAAAAIAa//qBGwGvQAPABoAABMQADMyFxEzESMnBiMiABEXFBYzMjcRJiMiBmsBCdbWfNC/Cn3e0/72z6uXxVtdwZmrAmoBFwFVkgJ5+UOCmAFZARcIz+mxAi6s7AAAAgBo/+oEbwTWABUAHQAABSIAETU0EjYzMgARFSEeATMyNjcXBgEiBgchNS4BApX3/sqL+JHtAQb8yQXJm26YOn+Z/rV+qxQCYQmZFgFEARAmtQEcof7H/t5XtN1aSmPqBEG3pQ+frgABAEMAAAMiBtUAFQAAIREjNTM1NDYzMhcHJiMiBh0BIRUhEQEDwMDRv0hHCzU8ZW4BBP78BB+gfcPWFKgKdm6AoPvhAAIAbP4iBG4E1gAZACQAABMQADMyFzczERQAIyImJzcWMzI2PQEGIyIAExQWMzI3ESYjIgZsAQfY33wKvv7p7IT7QmuGwZipfNjV/vjRqJjEXF+/mKoCagEcAVCeh/te7P7xcGJ8paubaI8BVwERzeuyAiuu7AAAAAEAnQAABFkGvQARAAABNjMgExEjES4BIyIGBxEjETMBbYrdAYID0AF2fWWYK9DQBCyq/k383QMkg35sV/yeBr0AAAAAAgCeAAABlAZ6AAMADwAAISMRMwM0NjMyFhUUBiMiJgF/0NDhPj08Pz88PT4EvwFCM0ZGMjNDQwAC/7f+FQGDBnoADAAYAAABERAhIic1FjMyNjURAzQ2MzIWFRQGIyImAXT+wkU6JDpGSRU9PD0+Pj09PAS/+rX+oRSnCUtdBVABQjJHRjIzQ0MAAAABAJ4AAASLBr0ADAAAAQcRIxEzETcBMwkBIwHwgtDQbwF7/P4oAg/zAjOI/lUGvfvthQGQ/gX9PAAAAAABAK8AAAF/Br0AAwAAISMRMwF/0NAGvQABAJwAAAdEBNYAHQAAARc2MzIXPgEzIBMRIxE0JiMiBgcRIxEQISIHESMRAWEFhuP/XDzDhAGQB9B3jHSZDNH+/c1L0AS/h57EWGz+WPzSAyKCgYp1/NoDHAEJrvyJBL8AAAAAAQCdAAAEWQTWABEAAAEXNjMgExEjES4BIyIGBxEjEQFiBozgAYID0AF2fWWYK9AEv5mw/k383QMkg35sV/yeBL8AAAACAGb/6gS4BNYADwAbAAATNBI2MzIAERUUAgYjIgARFxQWMzI2NTQmIyIGZo36ofgBMof9pPf+zdG8nZ67vp2avQJrsgEem/6o/uMPsf7mnQFXARsKyvX44Mj49QAAAAACAJ3+LQSgBNYADwAaAAABEAIjIicRIxEzFzYzMhIRJzQmIyIHERYzMjYEoP7Z3X/Qvgp/4tv/0K+ZvV5dwJawAlL+6/6tjP23BpKHnv61/toFze6n/bum7QAAAAIAa/4tBGsE1gAPABoAABMQADMyFzczESMRBiMiABEXFBYzMjcRJiMiBmsBBt7XfQm/0H7R3P77z7GVuWJjtpayAmoBHgFOkXr5bgJDhgFWARsJ0e6kAlOh7wAAAQCdAAAC6QTWAA0AAAEmIyIHESMRMxc2MzIXAukwN8xJ0MoEZrs9IAQFCK78oQS/jKMQAAEAa//qBDEE1gAmAAABNCYkLgE1NDYzMgQVIzQmIyIGFRQWBB4BFRQEIyIuATUzHgEzMjYDYX/+xLlZ/cTPAQHRkG9xgHcBN8Fe/vvQkuB/zwacgHaPAUJVXUNeg1qVz9amVXxjUEtMRmCIYqPEZ7lsaHtgAAAAAAEACv/qAqAF5QAVAAABETMVIxEUFjMyNxUGIyImNREjNTMRAbfj4z1JJD9STouO3d0F5f7aoP0OSUkNqBaomwLyoAEmAAAAAAEAmf/qBFYEvwAQAAAlBiMiJicRMxEQMzI3ETMRIwOLeevCywHP4e5P0MZ4juHeAxb87/7ssQN0+0EAAAEAJQAABC8EvwAGAAAJATMBIwEzAi4BLdT+TZ/+SNQBGgOl+0EEvwABADAAAAaKBL8ADAAAARMzASMJASMBMxMBMwTR6s/+n6n+2f7gqP6fz+8BG6cBHgOh+0EDmfxnBL/8cgOOAAAAAQAuAAAEQQS/AAsAAAkBMwkBIwkBIwkBMwI1AQ3z/nIBmvD+5/7o8gGa/nLxAwQBu/2n/ZoBx/45AmYCWQAAAAEAGf4VBCQEvwAPAAAJATMBAiMvATUXMjY/AQEzAisBG97+GHL3J044anYmLv5P4wEwA4/6hf7RBA6pBVZxewSyAAAAAQBjAAAEKAS/AAkAACUhFSE1ASE1IRUBYQLH/DsCnv1sA5mqqpkDeqyTAAABAEj+ZQLwBwIAGAAAASYCPQEQIzUyETU+ATcXBhEVFAcWERUSFwLGx8nu7gLFySrqvLwD5/5lOAEB0+ABEaMBD+rO/DqCS/7D4v9kZf7+5/7LSgABAMX+0QFsBmMAAwAAASMRMwFsp6f+0QeSAAAAAQAV/mUCvwcCABgAABM2EzUQNyYRNRAnNx4BFxUQMxUiERUUBgcV5AjLy+srx8gB7u7Lxf7mSQEr9wEDX1wBBeQBPUuCOfzQ7P70o/7v49P+OAAAAQCTAcMFigOFABcAAAEUBiMiLgIjIgYVBzQ2MzIeAR8BMjY1BYrSmVGQvlMvWF61z5xVncZIIVVrA2my9DymKXlpArPoSLULAoJqAAMAZv/oBqAGegAbACsAOwAAARQGIyImPQE0NjMyFhUjNCYjIgYdARQWMzI2NSUUEgQzMiQSNTQCJCMiBAIHNBIkMzIEEhUUAgQjIiQCBOnDsbDV1660waRrZmp5eWpnafyitAE1tLMBNLSy/su0tP7Ks4HSAXTY1wFz0sr+jN7e/o3NAp6rte3MfMXtuKdvX5uKf4abXnGVwf62uroBSsG/AUW8uf64v+MBhODg/nzj3v5+6+kBggACAHMAqgPPBCgABgANAAAJASMBNQEzEwEjATUBMwE6ASKe/rUBS55RASKf/rUBS58CZ/5DAbMWAbX+P/5DAbMWAbUAAAQAZf/oBp8GegAPAB8ANQA+AAATNBIkMzIEEhUUAgQjIiQCNxQSBDMyJBI1NAIkIyIEAgERIxEhMhYVFAcWFxUUFxUjJjQnJi8BMz4BNTQmKwFl0gF02NcBc9LK/ozf3f6MzIG0ATWztQE2sbH+yrWz/sqzAfeeATasvpCJAROjEAMSgcawUGNXcZsDM+MBhODg/nzj3v5+6+kBguDB/ra6vgFGwcABRLy5/rj+7P6FA7mTjIpJOK1FYCsSKNATbASQAks8UkUAAgBzAKsD5QQqAAYADQAACQEVASMJASEBFQEjCQEBEgFL/rWfASH+3wInAUv+taABIv7eBCr+SxX+SwG/AcD+SxX+SwG/AcAAAQBkAOcEVwTtAAsAABMJATcJARcJAQcJAWQBc/6PhgFxAXKF/pABc4b+jP6NAXEBegF4iv6IAXiK/oj+hooBe/6FAAADAFAAwQSxBU8AAwAPABsAAAEhNSEBNDYzMhYVFAYjIiYRNDYzMhYVFAYjIiYEsfufBGH9VUBAQEJCQEBAQEBAQkJAQEACos4BYTZISDY1RUX8oDVISDU0R0cAAAEAKgJiAk4DCwADAAABITUhAk793AIkAmKpAAABACoCYgJOAwsAAwAAASE1IQJO/dwCJAJiqQAAAQC3AtsFHAOFAAMAAAEhNSEFHPubBGUC26oAAAEAowLbBn8DhQADAAABITUhBn/6JAXcAtuqAAABAGwEtQGmBtIACAAAARcGBxUjNTQ2AS93aATObQbSUY6lmYJz4AAAAQA2BJcBbwa9AAgAABMnNjc1MxUOAax2aATNAW0El1GSoaKScOAAAAEAKP7CAWIAywAIAAATJzY3NTMVFAaedmcD0G/+wlKPpIRwceMAAAABAFkElwGTBr0ACAAAARUWFwcuASc1ASYFaHdWawIGvaWijlFI2W2YAAAAAAIAdQS1AxEG0gAIABEAAAEXBgcVIzU0NiUXBgcVIzU0NgE4d2gEzm0BuHdoBM5sBtJRjqWZgnPgSFGOpZmCc+AAAAAAAgBDBJcC1Qa9AAgAEQAAEyc2NzUzFQ4BBSc2NzUzFQ4BuXZpA84BbQEDdmkDzQFtBJdRkqGiknDgRFGSoaKScOAAAAACACj+rgKvARQACAARAAATJzY3NTMVFAYXJzY3NTMVFAaedmcD0G/4dmYD0W3+rlGarM+4ee1IUZqsz7h46wAAAQB5AKwCYwQqAAYAAAkBIwE1ATMBQQEinv60AUyeAmr+QgG0FQG1AAEAZACrAk8EKgAGAAAJARUBIwkBAQMBTP60nwEi/t4EKv5LFf5LAb8BwAAAAQCbAlkCZQRCAA0AABM0NjMyFh0BFAYjIiY1m31nZoB7amh9A2NifXpoKmJ7fGMAAgCm//QDkwDrAAsAFwAANzQ2MzIWFRQGIyImJTQ2MzIWFRQGIyImpkBAQENDQEBAAetAQEBCQkBAQG02SEg2NEVFNDZISDY0RUUAAAMApv/0BWUA6wALABcAIwAANzQ2MzIWFRQGIyImJTQ2MzIWFRQGIyImJTQ2MzIWFRQGIyImpkBAQENDQEBAAetAQEBCQkBAQAHSQEBAQkJAQEBtNkhINjRFRTQ2SEg2NEVFNDZISDY0RUUAAAEAdASjARwGvQAEAAABAyMTMwEcF5EBpwZA/mMCGgAAAgCZBJICZga9AAQACQAAAQMjEzMFAyMTMwE3InwBnQEvIX0BnQYk/m4CK5n+bgIrAAAAAAEAQgB8A9YFxAADAAA3JwEXt3UDH3V8SgT+SgAB/B4Fcv84BqIAFwAAAxQGIyIuAiMiBhUnNDYzMh4CMzI2NciKZy5EbTAfL0GLiGgoP2s6IzBABpV6lhZGDkY3CHidF0EUTTIAAAAAAfw+BXH+Ega7AAMAAAEjATP+ErP+3/sFcQFKAAH9HgVx/vIGuwADAAABMwEj/ff7/tOnBrv+tgAB/QsFcv5nBz8ADwAAASc+ATU0JiM3MhYVFAYPAf0kAVROZlQIp61XVwEFcqwFIiwrLHdzYERaDU8AAAAC+64Ffv7oBqkAAwAHAAABIwEzASMBM/3Dvv6p/AI+qP7s5wV+ASv+1QErAAH84P53/db/ZQALAAABNDYzMhYVFAYjIib84D49PD8/PD0+/u0yRkYyM0NDAAAAAAIAnP5sAZIE1AADAA8AABMzEyMTFAYjIiY1NDYzMha/vQ7a4j48PT8/PTw+AwD7bAXzM0ZGMzJDQwAAAAEAdv7tBHYFyAAhAAAlMjY3Mw4BBxEjESYCPQE0Ejc1MxUeARcjLgEjIgYdARQWApJwpwnEB96iz8nh48fPqdcHxAmhdp6urpOIZI7hHf76AQcmAT/3J+4BQCX++hruqXaY5Ngn0uMAAQBmAAAE8wZ6ACEAAAEXFAchByE1Mz4BNzUnIzUzAzQAMzIWFSM0JiMiBhUTIRUB+AlFAzcB+3lWLTgCCLq0CgET4dX51o99dZIKAWYCu/isZ7CwCpRrCfmwASTfAQzux3iLrYz+3LAAAAAAAgB2/+IGBAWNABsAKwAAJQYjIicHJzcmNTQ3JzcXNjMyFzcXBxYVFAcXBwEUHgEzMj4BNTQuASMiDgEE17Pr6LOWkpx1fqWSpbHb3bKnlap8c6GV++6B3X9+3H9/3X5+3YF+lJKZl5+v4+e3qpmph4irmq235NyzopgCyYruiYrtiontiIfuAAEAIwAABUAGYwAWAAAJATMBIRUhFSEVIREjESE1ITUhNSEBMwKyAZj2/isBXv5RAa/+Udn+WAGo/lgBX/4r9wNuAvX82I25i/6WAWqLuY0DKAAAAAIApf7RAXYGYwADAAcAABMRMxkBIxEzpdHR0f7RA3j8iAQ/A1MAAAACAGX91AUGBnoANABEAAABFAceARUUBCEiJicmNTcUFjMyNjU0JicuAjU0Ny4BNTQkMyAEFSM0JiMiBhUUHgEEHgIlJicOARUUHgEEFz4BNTQmBQbRTVH+3P8AfeJPnNHKr5m6n+vM2GjMSlABLPoBBAEk0LycoLU/mAFCvn9B/Z5lVFpUPJYBPzFYXpwB5NRgN5hxvOA/QH/mApKpg2xkdkY1fa590WM3mHG64f7mjK6Bbk5aSVlRbZHAGx4VcU5PWUtcExdxTmJ7AAACAHMFjANLBnsACwAXAAATNDYzMhYVFAYjIiYlNDYzMhYVFAYjIiZzPT09Pj49PT0B4j49PT4+PT0+BgQyRUUyM0NDMDNGRjIzQ0MAAgClAwgDbwZ6ABsAJQAAASYnBiMiJjU0NjsBNTQjIgYVJzQ2MzIWFREUFyUyNjc1Iw4BFRQCtg0HVo+GkrzBeYtOWLXBmpWtHf56MGMffl1kAxgmK2GLdH2GO5c8Og50kqGW/p1tW4otHqACRjppAAEAjwGlBDQDggAFAAABIxEhNSEENNH9LAOlAaUBKbQAAQAqAmICTgMLAAMAAAEhNSECTv3cAiQCYqkAAAEAhwXDA6kGYwADAAABITUhA6n83gMiBcOgAAACAJIENgLKBnoACwAXAAATNDYzMhYVFAYjIiYFMjY1NCYjIgYVFBaSp3d1paV1dqgBHj1TUz09VVUFVnSwrnZ3qakZUEBBVFk8PVMAAgBtAAAEcgWPAAsADwAAASEVIREjESE1IREzASE1IQLZAZn+Z7z+UAGwvAFo/FcDqQO/qf4vAdGpAdD6casAAAEASgLtAv8GcAAXAAABITUBNjU0JiMiBhUjNDYzMhYVFA8CIQL//V8BUXtIRFRQsLyUlK14XsYBwALteQE9c003RVZAgKePfHR5WKMAAAAAAQBGAuAC7AZuACYAAAEzMjY1NCYjIgYVIzQ2MzIWFRQGBxYVFAYjIiY1MxQWMzI2NTQnIwEqXlNRR05AVbC3i5qvTkqnv5mUurFZS09SsGIE70U2MkI6Lm6KiHQ+ZhwuoXeMjXgyREQ5fwIAAQCKBXECXga7AAMAAAEzASMBY/v+06cGu/62AAEArf4tBGoEvwASAAABER4BMzI3ETMRIycGIyInESMRAX0BdILfRtG8Cmi/pVvQBL/9ObevqwOC+0GBl1L98QaSAAAAAQBLAAADpgZjAAoAACERIyAANTQAKQERAtVe/v7+1gErAQIBLgJIAR3x7wEe+Z0AAQClArcBpwOwAAsAABM0NjMyFhUUBiMiJqVAQEBCQkBAQAMzNkdHNjVHRwABAIL+FwHeAAAADgAAIQcWFRQGIycyNjU0Jic3AUANq7OhCFliSG4kOh+kbX95OjUxLwuWAAAAAAEAiQL1AiwGawAGAAABIxEHNSUzAiyw8wGPFAL1AqNAkIMAAAIAiQMHA4oGegANABsAABM0NjMyFh0BFAYjIiY1FxQWMzI2PQE0JiMiBgeJ062u09KtrdW3bV5eam1dW2wCBO2y29m7U7La2rkHcICBcldvgHttAAQAXwAABkAGYAAGAAoAFQAZAAABIxEHNSUzAycBFxMzFSMVIzUhJwEzATMRBwICsPMBjxQQdQMedbh4eLD+WwcBp7X+VfsTAuoCo0CPhPolSgT+SvvNkr6+cgJG/doBRB8AAAADAFoAAAZ/BmAAAwAKACIAACUnARcBIxEHNSUzASE1ATY1NCYjIgYVIzQ2MzIWFRQPAiEBwHUDH3X9HrHyAY8UBIL9YAFQe0hDVU+xvJSUrXhexgHAhUoE/kr9ZwKjQI+E+aB5AT1zTTdFVkCApo58dXhYowAAAAAEAH0AAAaoBnAAAwAOABIAOQAAJScBFxMzFSMVIzUhJwEzATMRBwEzMjY1NCYjIgYVIzQ2MzIWFRQGBxYVFAYjIiY1MxQWMzI2NTQnIwKAdQMfdZB5ebD+WwcBqLT+VvoT+/VeU1FHTkBVsLeLmq9OSqe/mZS6sVlLT1KwYoVKBP5K+82Svr5yAkb92gFEHwJ8RDYzQToubouIdT5mHC6hd4yNeDJERDl/AgAAAgBM/lAD5QTUABgAJAAAAQ4DDwEUFjMyNjUzDgEjIiY1ND8BNjUTFAYjIiY1NDYzMhYClAEubM4NAoJ7cI3PAv3N3PG0ekvYPT09Pz89PT0C/HePhdhvKnqCf2e15eHJw8R/WKQBZDNFRTMxQ0MAAAL/8AAACD4GYwAPABIAACkBAyEDIwEhFSETIRUhEyEBIQMIPvwhEf2G5v4D3QQs/PgWApf9cBgDGPoIAgAjAYz+dAZjq/3vqf2sAaYDNwAAAwCF/5gFvganABcAIAApAAABFAIEIyInByMTJhE1NBIkMzIXNzMDFhMFEBcBJiMiAgMFNCcBFjMyEhMFqKL+18XAk22gotWjASzC8KZ0nrKZA/uzbgJ5crrN6wMDdj/9mGaI0eUDAv3x/pq8XK4BA9gBlF3sAWrAjbr+4dL+tm/+7p8D+Hz+yf7sD82T/CJHATABHAAAAAACALoAAATmBmMADQAWAAABESEyHgEVFAAjIREjERMRITI2NTQmJwGLATql94X+2P/+zNHRATegtLSZBmP+t3bajdr+/f6gBmP+DP2bqoeKqQEAAAABAJz/6gT1BtEAKgAAISMRNBIzMhYVFAYVFB4CFRQGIyImJzceATMyNjU0LgI1NDY1NCYjIhEBbNDp0MvdkFTTYeTMW8wqMDeYO3h/U9RinHRj9QTg6QEIybKN5E05a6KZVbPHMR+uIzJqXDpspZtbZOhfaXn+tgADAFj/6gdIBNYAKgA1AD0AAAUgJw4BIyImNTQ2OwE1NCYjIgYVJzQkMzIWFz4BMzIAERUhHgEzMj8BFwYlMjY3ESEOARUUFgEiBgchNTQmBYn+25lJ/Z+70//4+nt1dp3PARDSgcY4R8N27AEE/M8Iw6emiDVIsfuMUbE4/wCDnngDuIGoEwJclhbKYWnCqbHDYHiKfFsWoMxeXVli/t/++oHG1lUjmYeoUz0BCgJ8XVdoA5nAnCOUpQAAAAIAjv/qBLEG7gAdACsAAAEAERUUAgYjIi4BNTQSNjMyFyYnByc3Jic3BBc3FwMnLgEjIgYVFBYzMjY1A5kBGITyl5j3h33pkbeINp/1UdeUzkABDcTVUnUCJpxno7a8kIysBbX+2P40aLL+5KGQ/JamAQWSgNufp3CTZjeyPJyRcfyTP0VS17yd3P7PAAAAAAEAvgV+A2UGvQAIAAABFSMnByM1ATMDZayop6wBFH4FiQu/vw4BMQABAJ0FfQNTBrwACAAAATczFQEjATUzAfeps/7jf/7msQX9vwv+zAE0CwAAAAEAhwXDA6kGYwADAAABITUhA6n83gMiBcOgAAABADYElwFvBr0ACAAAEyc2NzUzFQ4BrHZoBM0BbQSXUZKhopJw4AAAAQCRBWIDMgaPAA0AAAEUBiMiJjUzFBYzMjY1AzK6lpe6qVZSTlkGj4ilpodPWFdQAAAAAAEAngWJAZQGdwALAAATNDYzMhYVFAYjIiaePj08Pz88PT4F/zNFRTIzREQAAgCIBUgCawcXAAsAFwAAARQGIyImNTQ2MzIWBRQWMzI2NTQmIyIGAmuLZ2eKimhniv6MSzc2TUw3OEoGLWGEhWFgiYlhNUxKNzdOTwAAAAABADj+GgHDAD8AEAAAIQcGFRQzMjcXBiMiJjU0NjcBrUF/WDU7Dk5lZHSXijNmYFEdiDF1YGStPwAAAAABAIoFcgOkBqIAFwAAARQGIyIuAiMiBhUnNDYzMh4CMzI2NQOkimcuRG0wHy5Ci4hoKD9rOiMwQAaVepYWRg5GNwh4nRdBFE0yAAAAAgBqBWgDkAa8AAMABwAAATMBIwMzAyMCp+n+0r563fWoBrz+rAFU/qwAAAACAI7+OQIP/6wACwAXAAATNDYzMhYVFAYjIiY3FBYzMjY1NCYjIgaOcFNQbmxSVW5hOycnNjYnKjj+8E9tbFBOaWpNJzY2Jyk4OwAAAQCjAtsGfwOFAAMAAAEhNSEGf/okBdwC26oAAAIAD/46BBMAAAADAAcAAAEhNSE1ITUhBBP7/AQE+/wEBP46qnKqAAABAE8AAASmBmMACwAAASERIxEhNSERMxEhBKb+O9H+PwHB0QHFBBP77QQTrAGk/lwAAAAAAQBi/i0EuAZjABMAACkBESMRITUhESE1IREzESEVIREhBLj+OND+QgG+/kIBvtAByP44Acj+LQHTqgNprAGk/lys/JcAAAAAAQArAmEA6AMNAAMAABMjNTPovb0CYawABgBM/+gIPgZ7ABUAIwAnADUAQwBRAAABNDYzMhc2MzIWHQEUBiMiJwYjIiY1ATQ2MzIWHQEUBiMiJjUBJwEXAxQWMzI2PQE0JiMiBhUFFBYzMjY9ATQmIyIGFQEUFjMyNj0BNCYjIgYVA5y8k6tWWamUvLuSrFlVqpK//LC8k5S8uZWSvwGWdQMedcljU1FhYlJQZAIEY1JQYWJSUWH6rGNTUGJhU1FjAZGTvoiIvZxQk76Hh7ycA+mTv7+ZUZG/vJz7okoE/kr7v1hyb2BTWXBvX1NYcnNcU1lwcF4DRldybmBSV3NyXQAAAAAEALT/9AP5BmMAAwAPABMAHwAAASMDMwM0NjMyFhUUBiMiJgEjAzMDNDYzMhYVFAYjIiYBhrwO2eE9PT0/Pz09PQMivA/a4T09PT4+PT09Ac4ElfoFM0REMjNCQgGYBJX6BTNERDIzQkIAAAACAHf/6AS2BqcAGwAqAAABMhYXLgIjIgcnNzYzIAARFRQCBiMiABE1NAAFIgYdARQWMzISPQEnLgECgmm6QQ92u2uRrhI3gqkBKAFCh/mi9P7XASABAJ6yspugsgQgswR8V0yd9IhDqhg2/hj+PTjU/rK6AUcBFBD3ATKr0rQSv+gBGPZEEWV3AAAAAAIAIwAABh8GYwADAAYAAAEzASElIQEC1b8Ci/oEASYDtP41BmP5nbAEqwAAAQC+/xEFfwZjAAcAAAUjESERIxEhBX/Q/N/QBMHvBqf5WQdSAAAAAAEATf7SBT4GYwAMAAAJASEVITUJATUhFSEBA9f9cwP0+w8CrP1UBJv8YQKOAoj89augAyUDKqKr/OwAAAEAvQLbBGYDhQADAAABITUhBGb8VwOpAtuqAAABAEcAAAUpBmMACAAACQEzASMBIzUhAnUB39X9n57+7dABYgE/BST5nQLBrQAAAAADAG7/6AjABNYAHAAsADwAAAEUAgYjIiQnBgQjIiYCPQE0EjYzMgQXNiQzMgARBRQWMzI2PwE1LgIjIgYVJTQmIyIGDwEVHgIzMjY1CMCN+pqj/vVaW/73opr5kI76maMBCltaAQyk5wE4+H66mYDQOg0bgKRal7oGs7uVgdM8ChmDolqYugJQpf7go8/Hyc2hASCpG6UBIKTOycfQ/pn+7w7H99O3LC9v2G330AnD+9S9Iy9t3Wz5zwAAAAH/pv4VAt4G1QAVAAAFFAYjIic3FjMyNRE0NjMyFwcmIyIVAZG4skBBFDQlrse1Q14aKj3MeLa9F6MPxwW5v9YYnwz2AAAAAAIAcQE6BIoEbgAXAC8AABM+ATsBMh8BFjMyNxUGIyIvASYrASIGBwM+ATsBMh8BFjMyNxcGIyIvASYrASIGB3M1lEoNVU2rSleXcnOWV0q1R1AOSpQ1AjaSSglYT6dMWpZyAXOWV0uqT1cJS5M2A/Q5QSVXI5DWeiNdIExD/v46QiVXJZDUeyNYJUxEAAAAAAEAqwCuBFMFbQATAAABIQMnNyM1IRMhNSETFwczFSEDIQRT/ayfa3nDASyn/i0CPKxrhtv+vKYB6gHA/u5C0LQBHrUBJkLktf7iAAACAEYAAgPvBMMABgAKAAAJARUBNQEVEyE1IQEoAsD8aQOXB/xXA6kC+P8AxgF/lAF+xvwFqgAAAAIAlQABBFYE2AAGAAoAAAkBNQEVATUBITUhA339GQPA/EADqfxWA6oDEgEGwP6ClP6Bwv34qgAAAAAeAW4AAQAAAAAAAABOAAAAAQAAAAAAAQALAFUAAQAAAAAAAgAHAE4AAQAAAAAAAwAYAFUAAQAAAAAABAALAFUAAQAAAAAABQA9AG0AAQAAAAAABgALAFUAAQAAAAAABwA+AKoAAQAAAAAACAAiABYAAQAAAAAACQAPACgAAQAAAAAACgBNAOgAAQAAAAAACwAXAWgAAQAAAAAADAAdATUAAQAAAAAADQBCAVIAAQAAAAAADgAXAWgAAwABBAkAAACcAZQAAwABBAkAAQAWAj4AAwABBAkAAgAOAjAAAwABBAkAAwAwAj4AAwABBAkABAAWAj4AAwABBAkABQB6Am4AAwABBAkABgAWAj4AAwABBAkABwB8AugAAwABBAkACABEAcAAAwABBAkACQAeAeQAAwABBAkACgCaA2QAAwABBAkACwAuBGQAAwABBAkADAA6A/4AAwABBAkADQCEBDgAAwABBAkADgAuBGRDb3B5cmlnaHQgKGMpIDIwMTcgYnkgd3d3LmZvbnRpcmFuLmNvbSAoTW9zbGVtIEVicmFoaW1pKS4gQWxsIHJpZ2h0cyByZXNlcnZlZC5SZWd1bGFySVJBTlNhbnNXZWI6VmVyc2lvbiA1LjAwVmVyc2lvbiA1LjAwO1NlcHRlbWJlciA1LCAyMDE3O0ZvbnRDcmVhdG9yIDExLjAuMC4yNDAzIDY0LWJpdElSQU5TYW5zIGlzIGEgdHJhZGVtYXJrIG9mIHd3dy5mb250aXJhbi5jb20gKE1vc2xlbSBFYnJhaGltaSkuVG8gdXNlIHRoaXMgZm9udCwgaXQgaXMgbmVjZXNzYXJ5IHRvIG9idGFpbiB0aGUgbGljZW5zZSBmcm9tIHd3dy5mb250aXJhbi5jb21odHRwOi8vd3d3Lm1vc2xlbWVicmFoaW1pLmNvbUNvcHlyaWdodCAoYykgMjAxNyBieSBodHRwOi8vd3d3LmZvbnRpcmFuLmNvbSBBbGwgUmlnaHRzIFJlc2VydmVkLgBDAG8AcAB5AHIAaQBnAGgAdAAgACgAYwApACAAMgAwADEANwAgAGIAeQAgAHcAdwB3AC4AZgBvAG4AdABpAHIAYQBuAC4AYwBvAG0AIAAoAE0AbwBzAGwAZQBtACAARQBiAHIAYQBoAGkAbQBpACkALgAgAEEAbABsACAAcgBpAGcAaAB0AHMAIAByAGUAcwBlAHIAdgBlAGQALgBSAGUAZwB1AGwAYQByAEkAUgBBAE4AUwBhAG4AcwBXAGUAYgA6AFYAZQByAHMAaQBvAG4AIAA1AC4AMAAwAFYAZQByAHMAaQBvAG4AIAA1AC4AMAAwADsAUwBlAHAAdABlAG0AYgBlAHIAIAA1ACwAIAAyADAAMQA3ADsARgBvAG4AdABDAHIAZQBhAHQAbwByACAAMQAxAC4AMAAuADAALgAyADQAMAAzACAANgA0AC0AYgBpAHQASQBSAEEATgBTAGEAbgBzACAAaQBzACAAYQAgAHQAcgBhAGQAZQBtAGEAcgBrACAAbwBmACAAdwB3AHcALgBmAG8AbgB0AGkAcgBhAG4ALgBjAG8AbQAgACgATQBvAHMAbABlAG0AIABFAGIAcgBhAGgAaQBtAGkAKQAuAFQAbwAgAHUAcwBlACAAdABoAGkAcwAgAGYAbwBuAHQALAAgAGkAdAAgAGkAcwAgAG4AZQBjAGUAcwBzAGEAcgB5ACAAdABvACAAbwBiAHQAYQBpAG4AIAB0AGgAZQAgAGwAaQBjAGUAbgBzAGUAIABmAHIAbwBtACAAdwB3AHcALgBmAG8AbgB0AGkAcgBhAG4ALgBjAG8AbQBoAHQAdABwADoALwAvAHcAdwB3AC4AbQBvAHMAbABlAG0AZQBiAHIAYQBoAGkAbQBpAC4AYwBvAG0AQwBvAHAAeQByAGkAZwBoAHQAIAAoAGMAKQAgADIAMAAxADcAIABiAHkAIABoAHQAdABwADoALwAvAHcAdwB3AC4AZgBvAG4AdABpAHIAYQBuAC4AYwBvAG0AIABBAGwAbAAgAFIAaQBnAGgAdABzACAAUgBlAHMAZQByAHYAZQBkAC4AAAACAAAAAAAA/agAZAAAAAAAAAAAAAAAAAAAAAAAAAAAAewAAAECAAIAAwEDAQQBBQEGAQcBCAEJAQoBCwEMAQ0BDgEPARABEQESARMBFAEVARYBFwEYARkBGgEbARwBHQEeAR8BIAEhASIBIwEkASUBJgEnASgBKQEqASsBLAEtAS4BLwEwATEBMgEzATQBNQE2ATcBOAE5AToBOwE8AT0BPgE/AUABQQFCAUMBRAFFAUYBRwFIAUkBSgFLAUwBTQFOAU8BUAFRAVIBUwFUAVUBVgFXAVgBWQFaAVsBXAFdAV4BXwFgAWEBYgFjAWQBZQFmAWcBaAFpAWoBawFsAW0BbgFvAXABcQFyAXMBdAF1AXYBdwF4AXkBegF7AXwBfQF+AX8BgAGBAYIBgwGEAYUBhgGHAYgBiQGKAYsBjAGNAY4BjwGQAZEBkgGTAZQBlQGWAZcBmAGZAZoBmwGcAZ0BngGfAaABoQGiAaMBpAGlAaYBpwGoAakBqgGrAawBrQGuAa8BsAGxAbIBswG0AbUBtgG3AbgBuQG6AbsBvAG9Ab4BvwHAAcEBwgHDAcQBxQHGAccByAHJAcoBywHMAc0BzgHPAdAB0QHSAdMB1AHVAdYB1wHYAdkB2gHbAdwB3QHeAd8B4AHhAeIB4wHkAeUB5gHnAegB6QHqAesB7AHtAe4B7wHwAfEB8gHzAfQB9QH2AfcB+AH5AfoB+wH8Af0B/gH/AgACAQICAgMCBAIFAgYCBwIIAgkCCgILAgwCDQIOAg8CEAIRAhICEwIUAhUCFgIXAhgCGQIaAhsCHAIdAh4CHwIgAiECIgIjAAQACwAMABEAHQAFAAYABwAIAAkACgANAA4ADwAQABIAEwAUABUAFgAXABgAGQAaABsAHAAeAB8AIAAhACIAIwAkACUAJgAnACgAKQAqACsALAAtAC4ALwAwADEAMgAzADQANQA2ADcAOAA5ADoAOwA8AD0APgA/AEAAQQBCAEMARABFAEYARwBIAEkASgBLAEwATQBOAE8AUABRAFIAUwBUAFUAVgBXAFgAWQBaAFsAXABdAF4AXwBgAGEAiwCpAIoAqgDwALgCJAIlAiYCJwIoAikCKgIrAiwCLQIuAi8CMACyALMAtgC3AMQCMQC0ALUAxQC+AL8AhwIyAKsCMwI0ALwCNQI2AjcCOAI5AjoAowCEAIUAvQCWAOgAhgCOAJ0ApAI7ANoAgwCTAPIA8wCNAJcAiADDAN4A8QCeAPUA9AD2AKIAkACRAO0AiQCgAOoA2ADhAjwCPQDbANwA3QDgANkA3wI+Aj8CQACCAMICQQDGAkIAmACoAJoAmQDvAKUAkgCcAKcAjwCUAJUETlVMTAd1bmkwNjIxB3VuaTA2MjcMdW5pMDYyNy5maW5hB3VuaTA2MkQMdW5pMDYyRC5pbml0DHVuaTA2MkQubWVkaQx1bmkwNjJELmZpbmEHdW5pMDYyRgd1bmkwNjMxDHVuaTA2MzEuZmluYQd1bmkwNjMzDHVuaTA2MzMuaW5pdAx1bmkwNjMzLm1lZGkMdW5pMDYzMy5maW5hB3VuaTA2MzUMdW5pMDYzNS5pbml0DHVuaTA2MzUubWVkaQx1bmkwNjM1LmZpbmEHdW5pMDYzNwx1bmkwNjM3LmluaXQMdW5pMDYzNy5tZWRpDHVuaTA2MzcuZmluYQd1bmkwNjM5DHVuaTA2MzkuaW5pdAx1bmkwNjM5Lm1lZGkMdW5pMDYzOS5maW5hDHVuaTA2NDMuaW5pdAx1bmkwNjQzLm1lZGkHdW5pMDY0Mwx1bmkwNjQzLmZpbmEHdW5pMDY0NAx1bmkwNjQ0LmluaXQMdW5pMDY0NC5tZWRpDHVuaTA2NDQuZmluYQd1bmkwNjQ1DHVuaTA2NDUuaW5pdAx1bmkwNjQ1Lm1lZGkMdW5pMDY0NS5maW5hB3VuaTA2NDcMdW5pMDY0Ny5pbml0DHVuaTA2NDcubWVkaQx1bmkwNjQ3LmZpbmEHdW5pMDY0OAx1bmkwNjQ4LmZpbmEHdW5pMDY0OQx1bmkwNjQ5LmZpbmEQdW5pMDY0NDA2MjcuaXNvbBB1bmkwNjQ0MDYyNy5maW5hB3VuaTA2NDAHdW5pMDY2RQx1bmkwNjZFLmZpbmEMdW5pMDY2Ri5pbml0DHVuaTA2NkYubWVkaRBmZWhfZG90bGVzcy5pc29sDHVuaTA2QTEuZmluYQd1bmkwNjZGDHVuaTA2NkYuZmluYQxvbmVkb3QuYWJvdmUNdHdvZG90cy5hYm92ZQ90aHJlZWRvdHMuYWJvdmUPdGhyZWVkb3RzLmJlbG93B3VuaTA2NjAHdW5pMDY2MQd1bmkwNjYyB3VuaTA2NjMHdW5pMDY2NAd1bmkwNjY1B3VuaTA2NjYHdW5pMDY2Nwd1bmkwNjY4B3VuaTA2NjkHdW5pMDZGNAd1bmkwNkY1B3VuaTA2RjYMdW5pMDZDMS5pbml0DHVuaTA2QzEubWVkaQd1bmkwNkJFB3VuaTA2RDIMdW5pMDZEMi5maW5hB3VuaTA2QTkMdW5pMDZBOS5maW5hB3VuaTA2QUYMdW5pMDZBRi5pbml0DHVuaTA2QUYubWVkaQx1bmkwNkFGLmZpbmEHdW5pMDZCQQx1bmkwNkJBLmZpbmEHdW5pMDY2QQd1bmkwNjFGB3VuaTA2MEMHdW5pMDYxQgd1bmkwNjZCB3VuaTA2NkMHdW5pMDY2RAd1bmkwNjRCB3VuaTA2NEQHdW5pMDY0RQd1bmkwNjRGB3VuaTA2NEMHdW5pMDY1MAd1bmkwNjUxB3VuaTA2NTIHdW5pMDY1Mwd1bmkwNjU0B3VuaTA2NTUHdW5pMDY3MAd1bmkwNjU2B3VuaTA2MTUMdW5pMDYyRi5maW5hBXdhc2xhDHVuaTA2NkUuaW5pdAx1bmkwNjZFLm1lZGkUYmVoX2RvdGxlc3NfYWx0LmluaXQUYmVoX2RvdGxlc3NfYWx0Lm1lZGkMdW5pMDZDMS5maW5hB3VuaTA2ODYMdW5pMDY4Ni5maW5hDG9uZWRvdC5iZWxvdw10d29kb3RzLmJlbG93DWFsZWZfYWx0Lmlzb2wNYWxlZl9hbHQuZmluYQd1bmkwNjIzDHVuaTA2MjMuZmluYQd1bmkwNjI1DHVuaTA2MjUuZmluYQd1bmkwNjIyDHVuaTA2MjIuZmluYQd1bmkwNjcxDHVuaTA2NzEuZmluYQd1bmkwNjI4DHVuaTA2MjguaW5pdAx1bmkwNjI4Lm1lZGkMdW5pMDYyOC5maW5hB3VuaTA2N0UMdW5pMDY3RS5maW5hDHVuaTA2N0UuaW5pdAx1bmkwNjdFLm1lZGkHdW5pMDYyQQx1bmkwNjJBLmluaXQMdW5pMDYyQS5tZWRpDHVuaTA2MkEuZmluYQd1bmkwNjJCDHVuaTA2MkIuaW5pdAx1bmkwNjJCLm1lZGkMdW5pMDYyQi5maW5hB3VuaTA2NzkMdW5pMDY3OS5pbml0DHVuaTA2NzkubWVkaQx1bmkwNjc5LmZpbmEHdW5pMDYyOQx1bmkwNjI5LmZpbmEMdW5pMDY4Ni5pbml0DHVuaTA2ODYubWVkaQd1bmkwNjJDDHVuaTA2MkMuaW5pdAx1bmkwNjJDLm1lZGkMdW5pMDYyQy5maW5hB3VuaTA2MkUMdW5pMDYyRS5pbml0DHVuaTA2MkUubWVkaQx1bmkwNjJFLmZpbmEHdW5pMDYzMAx1bmkwNjMwLmZpbmEHdW5pMDY4OAx1bmkwNjg4LmZpbmEHdW5pMDYzMgx1bmkwNjMyLmZpbmEHdW5pMDY5MQx1bmkwNjkxLmZpbmEHdW5pMDY5OAx1bmkwNjk4LmZpbmEHdW5pMDYzNAx1bmkwNjM0LmluaXQMdW5pMDYzNC5tZWRpDHVuaTA2MzQuZmluYQd1bmkwNjM2DHVuaTA2MzYuaW5pdAx1bmkwNjM2Lm1lZGkMdW5pMDYzNi5maW5hB3VuaTA2MzgMdW5pMDYzOC5pbml0DHVuaTA2MzgubWVkaQx1bmkwNjM4LmZpbmEHdW5pMDYzQQx1bmkwNjNBLmluaXQMdW5pMDYzQS5tZWRpDHVuaTA2M0EuZmluYQx1bmkwNkE5LmluaXQMdW5pMDZBOS5tZWRpB3VuaTA2NDEMdW5pMDY0MS5pbml0DHVuaTA2NDEubWVkaQx1bmkwNjQxLmZpbmEIdmVoLmlzb2wIdmVoLmluaXQIdmVoLm1lZGkIdmVoLmZpbmEMdW5pMDY0Mi5pbml0DHVuaTA2NDIubWVkaQd1bmkwNjQyDHVuaTA2NDIuZmluYQx1bmkwNjQ2LmluaXQMdW5pMDY0Ni5tZWRpB3VuaTA2NDYMdW5pMDY0Ni5maW5hB3VuaTA2RDUHdW5pMDZDMAx1bmkwNkMwLmZpbmEHdW5pMDYyNAx1bmkwNjI0LmZpbmEMdW5pMDY0OS5pbml0DHVuaTA2NDkubWVkaQx1bmkwNjRBLmluaXQMdW5pMDY0QS5tZWRpB3VuaTA2NEEMdW5pMDY0QS5maW5hB3VuaTA2Q0MMdW5pMDZDQy5pbml0DHVuaTA2Q0MubWVkaQx1bmkwNkNDLmZpbmEHdW5pMDZEMwx1bmkwNkQzLmZpbmEHdW5pMDYyNgx1bmkwNjI2LmluaXQMdW5pMDYyNi5tZWRpDHVuaTA2MjYuZmluYRB1bmkwNjQ0MDYyMy5pc29sEHVuaTA2NDQwNjIzLmZpbmEQdW5pMDY0NDA2MjUuaXNvbBB1bmkwNjQ0MDYyNS5maW5hEHVuaTA2NDQwNjIyLmlzb2wQdW5pMDY0NDA2MjIuZmluYRB1bmkwNjQ0MDY3MS5pc29sEHVuaTA2NDQwNjcxLmZpbmELaGVoX2FlLmZpbmEHdW5pMDZDMgx1bmkwNkMyLmZpbmEHdW5pMDZDMQd1bmkwNkMzDHVuaTA2QzMuZmluYQx1bmkwNkJFLmZpbmEMdW5pMDZCRS5pbml0DHVuaTA2QkUubWVkaQd1bmkwNkYwB3VuaTA2RjEHdW5pMDZGMgd1bmkwNkYzB3VuaTA2RjkHdW5pMDZGNwd1bmkwNkY4DHVuaTA2RjQudXJkdQx1bmkwNkY3LnVyZHULZmF0aGF0YW5fMDELZGFtbWF0YW5fMDELa2FzcmF0YW5fMDEIZmF0aGFfMDEIZGFtbWFfMDEIa2FzcmFfMDEJc2hhZGRhXzAxCHN1a3VuXzAxDEdodW5uYV9hYm92ZQt1bmkwNjUxMDY0Qwt1bmkwNjUxMDY0RAt1bmkwNjUxMDY0Qgt1bmkwNjUxMDY0RQt1bmkwNjUxMDY0Rgt1bmkwNjUxMDY1MAd1bmlGQzYzC3VuaTA2NTQwNjRCC3VuaTA2NTQwNjRFC3VuaTA2NTQwNjRDC3Vu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");
              doc.addFont("IRANSansWeb.ttf", "IRANSansWeb", "normal");
              doc.setFont("IRANSansWeb");
              let width = doc.internal.pageSize.getWidth();
              let finalY;
              doc.text(this.accessReport + " - " + this.dateNavText, width - 10, 10, "right");
              doc.autoTable({
                startY: 20,
                body: [[this.reportedService.id, this.s20],
                  [this.reportedService.name, this.s28],
                  [this.reportedService.serviceId, this.s30],
                  [this.reportedService.description, this.s31]],
                styles: {
                  font: "IRANSansWeb",
                  halign: "right",
                },
              });

              let tempArray = [];
              let tempElement = [];
              for(let i = 0; i < this.allowedUsersReportList.length; ++i){
                tempElement.push(this.allowedUsersReportList[i].displayName.toString());
                tempElement.push(this.allowedUsersReportList[i].userId.toString());
                tempElement.push(this.allowedUsersReportList[i].recordNumber.toString());
                tempArray.push(tempElement);
                tempElement = [];
              }
              finalY = doc.lastAutoTable.finalY;
              doc.text(this.s76, width - 10, finalY + 10, "right");
              doc.autoTable({
                startY: finalY + 20,
                head: [[this.displayNameFaText, this.s20, "#"]],
                body: tempArray,
                styles: {
                  font: "IRANSansWeb",
                  halign: "right",
                },
                headStyles: {
                  fillColor: "28a745",
                  textColor: "ffffff",
                },
              });

              tempArray = [];
              tempElement = [];
              for(let i = 0; i < this.bannedUsersReportList.length; ++i){
                tempElement.push(this.bannedUsersReportList[i].displayName.toString());
                tempElement.push(this.bannedUsersReportList[i].userId.toString());
                tempElement.push(this.bannedUsersReportList[i].recordNumber.toString());
                tempArray.push(tempElement);
                tempElement = [];
              }
              finalY = doc.lastAutoTable.finalY;
              doc.text(this.s79, width - 10, finalY + 10, "right");
              doc.autoTable({
                startY: finalY + 20,
                head: [[this.displayNameFaText, this.s20, "#"]],
                body: tempArray,
                styles: {
                  font: "IRANSansWeb",
                  halign: "right",
                },
                headStyles: {
                  fillColor: "dc3545",
                  textColor: "ffffff",
                },
              });

              tempArray = [];
              tempElement = [];
              for(let i = 0; i < this.allowedGroupsReportList.length; ++i){
                for(let j = 0; j < this.userSearch.length; ++j){
                  if(typeof this.userSearch[j].memberOf !== "undefined" && this.userSearch[j].memberOf !== null){
                    if(this.userSearch[j].memberOf.includes(this.allowedGroupsReportList[i].id)){
                      tempElement.push(this.userSearch[j].displayName.toString());
                      tempElement.push(this.userSearch[j].userId.toString());
                      tempElement.push(this.allowedGroupsReportList[i].name.toString());
                      tempElement.push(this.allowedGroupsReportList[i].id.toString());
                      tempElement.push(this.allowedGroupsReportList[i].recordNumber.toString());
                      tempArray.push(tempElement);
                      tempElement = [];
                    }
                  }
                }
              }
              finalY = doc.lastAutoTable.finalY;
              doc.text(this.allowedGroupsText, width - 10, finalY + 10, "right");
              doc.autoTable({
                startY: finalY + 20,
                head: [[this.userFaNameInGroupText, this.userIdInGroupText, this.faNameText, this.enNameText, "#"]],
                body: tempArray,
                styles: {
                  font: "IRANSansWeb",
                  halign: "right",
                },
                headStyles: {
                  fillColor: "28a745",
                  textColor: "ffffff",
                },
              });

              tempArray = [];
              tempElement = [];
              for(let i = 0; i < this.bannedGroupsReportList.length; ++i){
                for(let j = 0; j < this.userSearch.length; ++j){
                  if(typeof this.userSearch[j].memberOf !== "undefined" && this.userSearch[j].memberOf !== null){
                    if(this.userSearch[j].memberOf.includes(this.bannedGroupsReportList[i].id)){
                      tempElement.push(this.userSearch[j].displayName.toString());
                      tempElement.push(this.userSearch[j].userId.toString());
                      tempElement.push(this.bannedGroupsReportList[i].name.toString());
                      tempElement.push(this.bannedGroupsReportList[i].id.toString());
                      tempElement.push(this.bannedGroupsReportList[i].recordNumber.toString());
                      tempArray.push(tempElement);
                      tempElement = [];
                    }
                  }
                }
              }
              finalY = doc.lastAutoTable.finalY;
              doc.text(this.bannedGroupsText, width - 10, finalY + 10, "right");
              doc.autoTable({
                startY: finalY + 20,
                head: [[this.userFaNameInGroupText, this.userIdInGroupText, this.faNameText, this.enNameText, "#"]],
                body: tempArray,
                styles: {
                  font: "IRANSansWeb",
                  halign: "right",
                },
                headStyles: {
                  fillColor: "dc3545",
                  textColor: "ffffff",
                },
              });

              doc.save("Parsso-ServiceReport-" + vm.reportedService.name + ".pdf");
            }else if(format === "xlsx"){
              let fileName = "Parsso-ServiceReport-" + this.reportedService.name;
              let fileData = [[this.s20,this.reportedService.id,"",this.s28,this.reportedService.name,"",this.s30,this.reportedService.serviceId,"",this.s31,this.reportedService.description],
                [""],
                ["",this.s76,"","","",this.s79,"","","","",this.allowedGroupsText,"","","","","",this.bannedGroupsText],
                ["#",this.s20,this.displayNameFaText,"","#",this.s20,this.displayNameFaText,"","#",this.enNameText,this.faNameText,this.userIdInGroupText,this.userFaNameInGroupText,
                  "","#",this.enNameText,this.faNameText,this.userIdInGroupText,this.userFaNameInGroupText]];

              let tempArray11 = [];
              let tempElement11 = {};
              let tempArray = [];
              let tempElement = {};

              for(let i = 0; i < this.allowedGroupsReportList.length; ++i){
                for(let j = 0; j < this.userSearch.length; ++j){
                  if(typeof this.userSearch[j].memberOf !== "undefined" && this.userSearch[j].memberOf !== null){
                    if(this.userSearch[j].memberOf.includes(this.allowedGroupsReportList[i].id)){
                      tempElement.displayName = this.userSearch[j].displayName.toString();
                      tempElement.userId = this.userSearch[j].userId.toString();
                      tempElement.name = this.allowedGroupsReportList[i].name.toString();
                      tempElement.id = this.allowedGroupsReportList[i].id.toString();
                      tempElement.recordNumber = this.allowedGroupsReportList[i].recordNumber.toString();
                      tempArray.push(tempElement);
                      tempElement = {};
                    }
                  }
                }
              }

              for(let i = 0; i < this.bannedGroupsReportList.length; ++i){
                for(let j = 0; j < this.userSearch.length; ++j){
                  if(typeof this.userSearch[j].memberOf !== "undefined" && this.userSearch[j].memberOf !== null){
                    if(this.userSearch[j].memberOf.includes(this.bannedGroupsReportList[i].id)){
                      tempElement11.displayName = this.userSearch[j].displayName.toString();
                      tempElement11.userId = this.userSearch[j].userId.toString();
                      tempElement11.name = this.bannedGroupsReportList[i].name.toString();
                      tempElement11.id = this.bannedGroupsReportList[i].id.toString();
                      tempElement11.recordNumber = this.bannedGroupsReportList[i].recordNumber.toString();
                      tempArray11.push(tempElement11);
                      tempElement11 = {};
                    }
                  }
                }
              }

              let maxLength = Math.max(this.allowedUsersReportList.length, this.bannedUsersReportList.length, tempArray.length, tempArray11.length);
              let tempArray1 = new Array(maxLength - this.allowedUsersReportList.length).fill({"recordNumber":"", "userId":"", "displayName":""});
              let tempArray2 = new Array(maxLength - this.bannedUsersReportList.length).fill({"recordNumber":"", "userId":"", "displayName":""});
              let tempArray3 = new Array(maxLength - tempArray.length).fill({"recordNumber":"", "id":"", "name":"", "userId":"", "displayName":""});
              let tempArray4 = new Array(maxLength - tempArray11.length).fill({"recordNumber":"", "id":"", "name":"", "userId":"", "displayName":""});
              tempArray1 = this.allowedUsersReportList.concat(tempArray1);
              tempArray2 = this.bannedUsersReportList.concat(tempArray2);
              tempArray3 = tempArray.concat(tempArray3);
              tempArray4 = tempArray11.concat(tempArray4);
              for(let i = 0; i < maxLength; ++i){
                fileData.push([tempArray1[i].recordNumber, tempArray1[i].userId, tempArray1[i].displayName, "",
                  tempArray2[i].recordNumber, tempArray2[i].userId, tempArray2[i].displayName, "",
                  tempArray3[i].recordNumber, tempArray3[i].id, tempArray3[i].name, tempArray3[i].userId, tempArray3[i].displayName, "",
                  tempArray4[i].recordNumber, tempArray4[i].id, tempArray4[i].name, tempArray4[i].userId, tempArray4[i].displayName]);
              }
              let wb = XLSX.utils.book_new();
              wb.Props = {
                Title: fileName,
                Subject: fileName,
                Author: "Parsso",
              };
              wb.Views = {
                RTL: true,
              };
              wb.SheetNames.push(fileName);
              let ws = XLSX.utils.aoa_to_sheet(fileData);
              wb.Sheets[fileName] = ws;
              let wbout = XLSX.write(wb, {bookType:"xlsx",  type: "binary"});
              let fileBlob = new Blob([this.s2ab(wbout)],{type:"application/octet-stream"});
              if (window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(fileBlob, fileName + ".xlsx");
              }else {
                let a = document.createElement("a"),
                    url = URL.createObjectURL(fileBlob);
                a.href = url;
                a.download = fileName + ".xlsx";
                document.body.appendChild(a);
                a.click();
                setTimeout(function() {
                  document.body.removeChild(a);
                  window.URL.revokeObjectURL(url);
                }, 0);
              }
            }
            this.exportReportLoader = false;
          }
        },
        dailyAccessController: function (input) {
          if(input === "selectAll"){
            for(let i = 0; i < 7; ++i){
              document.getElementById("dailyAccessCheckbox" + i).checked = true;
              document.getElementById("dailyAccessContainer" + i).style.visibility = "visible";
            }
            document.getElementsByName("dailyAccess")[0].setAttribute("value", "0123456");
            document.getElementById("dailyAccessSelectAll").classList.add("d-none");
            document.getElementById("dailyAccessRemoveAll").classList.remove("d-none");
          }else if(input === "removeAll"){
            for(let i = 0; i < 7; ++i){
              document.getElementById("dailyAccessCheckbox" + i).checked = false;
              document.getElementById("dailyAccessContainer" + i).style.visibility = "hidden";
            }
            document.getElementsByName("dailyAccess")[0].setAttribute("value", "");
            document.getElementById("dailyAccessSelectAll").classList.remove("d-none");
            document.getElementById("dailyAccessRemoveAll").classList.add("d-none");
          }else{
            if(document.getElementById("dailyAccessCheckbox" + input).checked){
              document.getElementById("dailyAccessContainer" + input).style.visibility = "visible";
              document.getElementsByName("dailyAccess")[0].setAttribute("value",
                  document.getElementsByName("dailyAccess")[0].getAttribute("value") + input);
              if(document.getElementsByName("dailyAccess")[0].getAttribute("value").length === 7){
                document.getElementById("dailyAccessSelectAll").classList.add("d-none");
                document.getElementById("dailyAccessRemoveAll").classList.remove("d-none");
              }
            }else {
              document.getElementById("dailyAccessContainer" + input).style.visibility = "hidden";
              document.getElementsByName("dailyAccess")[0].setAttribute("value",
                  document.getElementsByName("dailyAccess")[0].getAttribute("value").replace(input, ""));
              document.getElementById("dailyAccessSelectAll").classList.remove("d-none");
              document.getElementById("dailyAccessRemoveAll").classList.add("d-none");
            }
          }
        },
        changeLang: function () {
          if(this.lang == "EN"){
            window.localStorage.setItem("lang", "EN");
            this.margin = "margin-left: 30px;";
            this.margin1 = "mr-1";
            this.lang = "فارسی";
            this.isRtl = false;
            this.dateNavText = this.dateNavEn;
            this.s0 = "IOOC Centralized Authentication";
            this.s1 = this.nameEN;
            this.s2 = "Exit";
            this.s3 = "Dashboard";
            this.s4 = "Services";
            this.s5 = "Groups";
            this.s6 = "Events";
            this.s7 = "Profile";
            this.s8 = "Connected Services";
            this.s9 = "Services";
            this.s10 = "Rules";
            this.s11 = "Privacy";
            this.s12 = "Guide";
            this.s13 = "Create New Service";
            this.s14 = "New";
            this.s15 = "Users";
            this.s20 = "ID";
            this.s21 = "Name";
            this.s22 = "URL";
            this.s23 = "Edit";
            this.s24 = "Delete";
            this.s25 = "Basics";
            this.s26 = "Enable Service";
            this.s27 = "Service Type";
            this.s28 = "Service Name";
            this.s29 = "Service URL";
            this.s30 = "ServiceID";
            this.s31 = "Service Farsi Name";
            this.s32 = "Groups Access";
            this.s33 = "Reference Links";
            this.s34 = "Logo URL";
            this.s35 = "Information URL";
            this.s36 = "Privacy URL";
            this.s37 = "Contacts";
            this.s38 = "Name";
            this.s39 = "Email";
            this.s40 = "Phone";
            this.s41 = "Department";
            this.s42 = "Logout Options";
            this.s43 = "Logout URL";
            this.s44 = "Logout Type";
            this.s45 = "Submit";
            this.s46 = "Go Back";
            this.s47 = "Delete";
            this.s51 = "Configs";
            this.s54 = "Access Strategy";
            this.s55 = "Allow SSO";
            this.s56 = "Unauthorized Redirect Url";
            this.s57 =  " (Only English Letters And Numbers Are Allowed For Service Name)";
            this.s58 = "Basics";
            this.s59 = "Access Strategy";
            this.s60 = "Time Based Access";
            this.s61 = "Start Date";
            this.s62 = "Start Time";
            this.s63 = "End Date";
            this.s64 = "End Time";
            this.s65 = "Multi Factor Authentication";
            this.s66 = "MFA Enabled";
            this.s67 = " Example: 1399/05/01 ";
            this.s68 = " Example: 15:30 ";
            this.s69 = "Users Access";
            this.s70 = "Users List";
            this.s71 = "Search...";
            this.s72 = "Grant Access";
            this.s73 = "Revoke Access";
            this.s74 = "No User Found";
            this.s75 = "Search";
            this.s76 = "Authorized Users";
            this.s77 = "Remove";
            this.s78 = "List is Empty";
            this.s79 = "Banned Users";
            this.s80 = "Attribute Based Access";
            this.s81 = "Attribute Name";
            this.s82 = "Attribute Value";
            this.s83 = "Delete Service";
            this.s84 = "Action";
            this.s85 = "Are You Sure You Want To Delete Selected Services?";
            this.s86 = "No Service is Selected.";
            this.s87 = "Are You Sure You Want To Delete?";
            this.s88 = "Are You Sure You Want To Delete All Services?";
            this.s89 = " (If Entering The Address, Mention http Or https)";
            this.s90 = "Address";
            this.s91 = "File";
            this.s92 = "Records a Page: ";
            this.s93 = "Position";
            this.s94 = "Audits";
            this.s96 = "Remote Access";
            this.s97 = "Acceptable Response Codes";
            this.s98 = "Edit Service";
            this.s99 = "Hardware Token";
            this.s100 = "Disabled";
            this.s101 = "No Service Found";
            this.rolesText = "Roles";
            this.reportsText = "Reports";
            this.publicmessagesText = "Public Messages";
            this.ticketingText = "Ticketing";
            this.transcriptsText = "Access Reports";
            this.addAllGroupsText = "Select All";
            this.removeAllGroupsText = "Unselect All";
            this.inputEnglishFilterText = " (Only English Letters And Numbers Are Allowed)";
            this.inputPersianFilterText = " (Only Persian Letters And Numbers Are Allowed)";
            this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingText = "Meeting";
            this.enterMeetingText = "Enter Meeting";
            this.inviteToMeetingText = "Invite To Meeting";
            this.copyText = "Copy";
            this.returnText = "Return";
            this.dynamicOTPPassword = "Dynamic Password (OTP)";
            this.accessReport = "Service Access Reports";
            this.searchText = "Search...";
            this.displayNameFaText = "Full Name";
            this.userNotFoundText = "No User Found";
            this.groupNotFoundText = "No Group Found";
            this.allowedGroupsText = "Allowed Groups";
            this.bannedGroupsText = "Banned Groups";
            this.enNameText = "English Name";
            this.faNameText = "Farsi Name";
            this.returnText = "Return";
            this.serviceNotificationsText = "Service Notifications";
            this.apiAddressText = "API Address";
            this.apiKeyText = "API Key";
            this.userFaNameInGroupText = "Member's Name";
            this.userIdInGroupText = "Member's UserId";
            this.selectText = "Select";
            this.selectAllText = "Select All";
            this.removeText = "Unselect All";
            this.generateSecretText = "Generate Secret";
            this.accessByHoursAndDaysOfTheWeekText = "Daily Based Access";
            this.saturdayText = "Saturday";
            this.sundayText = "Sunday";
            this.mondayText = "Monday";
            this.tuesdayText = "Tuesday";
            this.wednesdayText = "Wednesday";
            this.thursdayText = "Thursday";
            this.fridayText = "Friday";
            this.daysText = "Days";
            this.fromText = "From: ";
            this.toText = "To: ";
            this.dayBasedText = "Day Based";
            this.urlBasedText = "URL Based";
          }else {
            window.localStorage.setItem("lang", "FA");
            this.margin = "margin-right: 30px;";
            this.margin1 = "ml-1";
            this.lang = "EN";
            this.isRtl = true;
            this.dateNavText = this.dateNav;
            this.s0 = "احراز هویت متمرکز شرکت نفت فلات قاره ایران";
            this.s1 = this.name;
            this.s2 = "خروج";
            this.s3 = "داشبورد";
            this.s4 = "سرویس ها";
            this.s5 = "گروه ها";
            this.s6 = "رویداد ها";
            this.s7 = "پروفایل";
            this.s8 = "سرویس های متصل";
            this.s9 = "سرویس ها";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "ایجاد سرویس جدید";
            this.s14 = "جدید";
            this.s15 = "کاربران";
            this.s20 = "شناسه";
            this.s21 = "نام";
            this.s22 = "آدرس";
            this.s23 = "ویرایش";
            this.s24 = "حذف";
            this.s25 = "تنظیمات پایه";
            this.s26 = "فعال سازی سرویس";
            this.s27 = "نوع سرویس";
            this.s28 = "نام سرویس";
            this.s29 = "آدرس سرویس";
            this.s30 = "شناسه سرویس";
            this.s31 = "نام فارسی سرویس";
            this.s32 = "دسترسی گروه ها";
            this.s33 = "پیوندهای مرجع";
            this.s34 = "آدرس لوگو";
            this.s35 = "آدرس راهنما";
            this.s36 = "آدرس قوانین";
            this.s37 = "اطلاعات تماس";
            this.s38 = "نام";
            this.s39 = "ایمیل";
            this.s40 = "شماره تماس";
            this.s41 = "دپارتمان";
            this.s42 = "تنظیمات خروج";
            this.s43 = "آدرس خروج";
            this.s44 = "نوع خروج";
            this.s45 = "تایید";
            this.s46 = "بازگشت";
            this.s47 = "حذف";
            this.s51 = "پیکربندی";
            this.s54 = "استراتژی دسترسی";
            this.s55 = "فعال سازی SSO";
            this.s56 = "آدرس صفحه مقصد در صورت مجاز نبودن دسترسی";
            this.s57 = " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)";
            this.s58 = "تنظیمات پایه";
            this.s59 = "استراتژی دسترسی";
            this.s60 = "دسترسی بر اساس زمان";
            this.s61 = "تاریخ شروع";
            this.s62 = "زمان شروع";
            this.s63 = "تاریخ پایان";
            this.s64 = "زمان پایان";
            this.s65 = "احراز هویت چند مرحله ای";
            this.s66 = "فعال سازی MFA";
            this.s67 = " مثال: 1399/05/01 ";
            this.s68 = " مثال: 20:30 ";
            this.s69 = "دسترسی کاربران";
            this.s70 = "لیست کاربران";
            this.s71 = "جستجو...";
            this.s72 = "اعطا دسترسی";
            this.s73 = "منع دسترسی";
            this.s74 = "کاربری یافت نشد";
            this.s75 = "جستجو کنید";
            this.s76 = "کاربران دارای دسترسی";
            this.s77 = "حذف";
            this.s78 = "لیست خالی است";
            this.s79 = "کاربران منع شده";
            this.s80 = "دسترسی بر اساس پارامتر";
            this.s81 = "نام پارامتر";
            this.s82 = "مقدار پارامتر";
            this.s83 = "حذف سرویس";
            this.s84 = "اعمال";
            this.s85 = "آیا از حذف سرویس های انتخاب شده اطمینان دارید؟";
            this.s86 = "هیچ سرویسی انتخاب نشده است.";
            this.s87 = "آیا از حذف این سرویس اطمینان دارید؟";
            this.s88 = "آیا از حذف تمامی سرویس ها اطمینان دارید؟";
            this.s89 = " (در صورت وارد کردن آدرس، http یا https ذکر شود)";
            this.s90 = "آدرس";
            this.s91 = "فایل";
            this.s92 = "تعداد رکورد ها: ";
            this.s93 = "موقعیت";
            this.s94 = "ممیزی ها";
            this.s96 = "دسترسی از راه دور";
            this.s97 = "کد پاسخ های قابل قبول";
            this.s98 = "ویرایش سرویس";
            this.s99 = "توکن سخت افزاری";
            this.s100 = "غیرفعال";
            this.s101 = "سرویسی یافت نشد";
            this.rolesText = "نقش ها";
            this.reportsText = "گزارش ها";
            this.publicmessagesText = "اعلان های عمومی";
            this.ticketingText = "پشتیبانی";
            this.transcriptsText = "گزارش های دسترسی";
            this.addAllGroupsText = "انتخاب همه";
            this.removeAllGroupsText = "لغو انتخاب همه";
            this.inputEnglishFilterText = " (تنها حروف انگلیسی و اعداد مجاز می باشند)";
            this.inputPersianFilterText = " (تنها حروف فارسی و اعداد مجاز می باشند)";
            this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingText = "جلسه مجازی";
            this.enterMeetingText = "ورود به جلسه";
            this.inviteToMeetingText = "دعوت به جلسه";
            this.copyText = "کپی";
            this.returnText = "بازگشت";
            this.dynamicOTPPassword = "رمز پویا (یکبار مصرف)";
            this.accessReport = "گزارش های دسترسی سرویس";
            this.searchText = "جستجو...";
            this.displayNameFaText = "نام کامل";
            this.userNotFoundText = "کاربری یافت نشد";
            this.groupNotFoundText = "گروهی یافت نشد";
            this.allowedGroupsText = "گروه های دارای دسترسی";
            this.bannedGroupsText = "گروه های منع شده";
            this.enNameText = "نام انگلیسی";
            this.faNameText = "نام فارسی";
            this.returnText = "بازگشت";
            this.serviceNotificationsText = "پیام های سرویس";
            this.apiAddressText = "آدرس API";
            this.apiKeyText = "کلید API";
            this.userFaNameInGroupText = "نام فارسی کاربر عضو";
            this.userIdInGroupText = "شناسه کاربر عضو";
            this.selectText = "انتخاب کنید";
            this.selectAllText = "انتخاب همه";
            this.removeText = "لغو انتخاب همه";
            this.generateSecretText = "ایجاد رمز";
            this.accessByHoursAndDaysOfTheWeekText = "دسترسی بر اساس ساعات و روزهای هفته";
            this.saturdayText = "شنبه";
            this.sundayText = "یکشنبه";
            this.mondayText = "دوشنبه";
            this.tuesdayText = "سه‌شنبه";
            this.wednesdayText = "چهارشنبه";
            this.thursdayText = "پنجشنبه";
            this.fridayText = "جمعه";
            this.daysText = "روز ها";
            this.fromText = "از: ";
            this.toText = "تا: ";
            this.dayBasedText = "بر اساس روز";
            this.urlBasedText = "بر اساس آدرس";
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
        sortedServices:function() {
          this.servicesPage = [];
          for(let i = 0; i < this.recordsShownOnPage; ++i){
              if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.services.length - 1){
                  this.servicesPage[i] = this.services[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
              }
          }
          return this.servicesPage;
        },
        resultQuery(){
          let buffer = [];
          if(this.searchQuery){
            buffer = buffer.concat(this.userSearch.filter((item)=>{
              if(typeof item.userId !== "undefined") {
                return this.searchQuery.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.userSearch.filter((item)=>{
              if(typeof item.displayName !== "undefined") {
                return this.searchQuery.split(" ").every(v => item.displayName.includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else{
            return buffer;
          }
        },
        allowedResultQuery(){
          let buffer = [];
          if(this.userAllowed.length !== 0){
            buffer = buffer.concat(this.userAllowed.filter((item)=>{
              if(typeof item.userId !== "undefined"){
                return this.searchQueryAllowedList.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.userAllowed.filter((item)=>{
              if(typeof item.displayName !== "undefined"){
                return this.searchQueryAllowedList.split(" ").every(v => item.displayName.includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else {
            return buffer;
          }
        },
        blockedResultQuery(){
          let buffer = [];
          if(this.userBlocked.length !== 0){
            buffer = buffer.concat(this.userBlocked.filter((item)=>{
              if(typeof item.userId !== "undefined") {
                return this.searchQueryBlockedList.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.userBlocked.filter((item)=>{
              if(typeof item.displayName !== "undefined"){
                return this.searchQueryBlockedList.split(" ").every(v => item.displayName.includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else {
            return buffer;
          }
        },
        allowedUsersReportListResult(){
          let buffer = [];
          if(this.allowedUsersReportSearch){
            buffer = buffer.concat(this.allowedUsersReportList.filter((item)=>{
              if(typeof item.userId !== "undefined") {
                return this.allowedUsersReportSearch.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.allowedUsersReportList.filter((item)=>{
              if(typeof item.displayName !== "undefined") {
                return this.allowedUsersReportSearch.toLowerCase().split(" ").every(v => item.displayName.toLowerCase().includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else {
            return buffer;
          }
        },
        bannedUsersReportListResult(){
          let buffer = [];
          if(this.bannedUsersReportSearch){
            buffer = buffer.concat(this.bannedUsersReportList.filter((item)=>{
              if(typeof item.userId !== "undefined") {
                return this.bannedUsersReportSearch.toLowerCase().split(" ").every(v => item.userId.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.bannedUsersReportList.filter((item)=>{
              if(typeof item.displayName !== "undefined") {
                return this.bannedUsersReportSearch.toLowerCase().split(" ").every(v => item.displayName.toLowerCase().includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else{
            return buffer;
          }
        },
        allowedGroupsReportListResult(){
          let buffer = [];
          if(this.allowedGroupsReportSearch){
            buffer = buffer.concat(this.allowedGroupsReportList.filter((item)=>{
              if(typeof item.id !== "undefined") {
                return this.allowedGroupsReportSearch.toLowerCase().split(" ").every(v => item.id.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.allowedGroupsReportList.filter((item)=>{
              if(typeof item.description !== "undefined") {
                return this.allowedGroupsReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else{
            return buffer;
          }
        },
        bannedGroupsReportListResult(){
          let buffer = [];
          if(this.bannedGroupsReportSearch){
            buffer = buffer.concat(this.bannedGroupsReportList.filter((item)=>{
              if(typeof item.id !== "undefined") {
                return this.bannedGroupsReportSearch.toLowerCase().split(" ").every(v => item.id.toLowerCase().includes(v));
              }
            }));
            buffer = buffer.concat(this.bannedGroupsReportList.filter((item)=>{
              if(typeof item.description !== "undefined") {
                return this.bannedGroupsReportSearch.toLowerCase().split(" ").every(v => item.description.toLowerCase().includes(v));
              }
            }));
            let uniqueBuffer = [...new Set(buffer)];
            return uniqueBuffer;
          }else{
            return buffer;
          }
        },
      },
    });
  })