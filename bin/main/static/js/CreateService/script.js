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
      groups: [],
      users:[],
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
      attributeList: [],
      groupList: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      samls: false,
      serviceType: "CAS",
      dailyAccessType: "DAY",
      userPicture: "images/PlaceholderUser.png",
      overlayLoader: false,
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
      service: {},
      activeItem: "main",
      metaDataAddress: true,
      metaDataFile: false,
      s0: "احراز هویت متمرکز شرکت نفت فلات قاره ایران",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "پروفایل",
      s8: "ایجاد سرویس جدید",
      s9: "fa fa-arrow-right",
      s10: "قوانین",
      s11: "حریم خصوصی",
      s12: "راهنما",
      s13: "کاربران",
      s14: "تنظیمات پایه",
      s15: "فعال سازی سرویس",
      s16: "نوع سرویس",
      s17: "نام سرویس",
      s18: "آدرس سرویس",
      s19: "نام فارسی سرویس",
      s20: "پیوندهای مرجع",
      s21: "آدرس لوگو",
      s22: "آدرس راهنما",
      s23: "آدرس قوانین",
      s24: "اطلاعات تماس",
      s25: "نام",
      s26: "ایمیل",
      s27: "شماره تماس",
      s28: "دپارتمان",
      s29: "تنظیمات خروج",
      s30: "آدرس خروج",
      s31: "نوع خروج",
      s32: "تایید",
      s33: "./dashboard",
      s34: "./services",
      s35: "دسترسی گروه ها",
      s36: "شناسه سرویس",
      s37: "./users",
      s38: "./groups",
      s39: "./profile",
      s40: "./privacy",
      s41: "پیکربندی",
      s42: "./configs",
      s43: "./events",
      s44: "استراتژی دسترسی",
      s45: "فعال سازی SSO",
      s46: "آدرس صفحه مقصد در صورت مجاز نبودن دسترسی",
      s47: " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)",
      s48: "تنظیمات پایه",
      s49: "استراتژی دسترسی",
      s50: "دسترسی بر اساس زمان",
      s51: "تاریخ شروع",
      s52: "زمان شروع",
      s53: "تاریخ پایان",
      s54: "زمان پایان",
      s55: "احراز هویت چند مرحله ای",
      s56: "فعال سازی MFA",
      s57: " مثال: 1399/05/01 ",
      s58: " مثال: 20:30 ",
      s59: "دسترسی کاربران",
      s60: "لیست کاربران",
      s61: "جستجو...",
      s62: "اعطا دسترسی",
      s63: "منع دسترسی",
      s64: "کاربری یافت نشد",
      s65: "جستجو کنید",
      s66: "کاربران دارای دسترسی",
      s67: "حذف",
      s68: "لیست خالی است",
      s69: "کاربران منع شده",
      s70: "دسترسی بر اساس پارامتر",
      s71: "نام پارامتر",
      s72: "مقدار پارامتر",
      s73: " (در صورت وارد کردن آدرس، http یا https ذکر شود)",
      s74: "آدرس",
      s75: "فایل",
      s76: "بازگشت",
      s77: "موقعیت",
      s78: "ممیزی ها",
      s79: "/audits",
      s80: "دسترسی از راه دور",
      s81: "کد پاسخ های قابل قبول",
      s82: "توکن سخت افزاری",
      s83: "غیرفعال",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      addAllGroupsText: "انتخاب همه",
      removeAllGroupsText: "لغو انتخاب همه",
      allGroupsHolderText: "انتخاب همه",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
      publicmessagesText: "اعلان های عمومی",
      publicmessagesURLText: "./publicmessages",
      ticketingText: "پشتیبانی",
      ticketingURLText: "./ticketing",
      transcriptsText: "گزارش های دسترسی",
      transcriptsURLText: "./transcripts",
      inputEnglishFilterText: " (تنها حروف انگلیسی و اعداد مجاز می باشند)",
      inputPersianFilterText: " (تنها حروف فارسی و اعداد مجاز می باشند)",
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
      otpText: "رمز یکبار مصرف (Google Authenticator)",
      serviceNotificationsText: "پیام های سرویس",
      apiAddressText: "آدرس API",
      apiKeyText: "کلید API",
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
      smsText: "رمز پیامکی (SMS)",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
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
      isActive (menuItem) {
        return this.activeItem === menuItem
      },
      setActive (menuItem) {
        this.activeItem = menuItem
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
      getGroups: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/groups")
        .then((res) => {
          vm.groups = res.data;
        });
      },
      getUsersList: function (){
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/users")
        .then((res) => {
          vm.userSearch = res.data;
        });
      },
      addGroup: function (n) {
        //n = n.split("'").join("");
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
      addService: function () {
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

          for(let i = 0; i < this.indexList.length; ++i){
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
          if(logoFile.files[0]){
            let logoFileBodyFormData = new FormData();
            logoFileBodyFormData.append("file", logoFile.files[0]);
            axios({
              method: "post",
              url: url + "/api/services/icon",  //
              headers: {"Content-Type": "multipart/form-data"},
              data: logoFileBodyFormData,
            }).then((logoRes) => {
              if(logoRes.data !== ""){
                vm.service.logo = logoRes.data;
              }else{
                vm.service.logo = null;
              }
              if(vm.serviceType === "SAML"){
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
                        if(res.data != ""){
                          vm.service.metadataLocation = res.data;
                          axios({
                            method: "post",
                            url: url + "/api/services/saml", //
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
                            window.location.replace(vm.s34);
                          });
                        }
                      });
                    }
                  }else if(vm.metaDataAddress){
                    if(document.getElementsByName("metadataLocation")[0].value != ""){
                      vm.service.metadataLocation = document.getElementsByName("metadataLocation")[0].value;
                      axios({
                        method: "post",
                        url: url + "/api/services/saml", //
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
                      })
                          .then((res) => {
                            window.location.replace(vm.s34);
                          });
                    }else{
                      alert("لطفا قسمت های الزامی را پر کنید.");
                    }
                  }
                }else{
                  alert("لطفا قسمت های الزامی را پر کنید.");
                }
              }else if(vm.serviceType === "Oauth2"){
                let supportedGrantTypesArray = document.getElementsByName("supportedGrantTypes")[0].getAttribute("value").split(", ");
                let supportedResponseTypesArray = document.getElementsByName("supportedResponseTypes")[0].getAttribute("value").split(", ");
                vm.service.clientId = document.getElementsByName("clientId")[0].value;
                vm.service.clientSecret = document.getElementsByName("clientSecret")[0].value;
                vm.service.supportedGrantTypes = [ "java.util.HashSet", supportedGrantTypesArray ];
                vm.service.supportedResponseTypes = [ "java.util.HashSet", supportedResponseTypesArray ];

                if(vm.service.clientId !== "" && vm.service.clientSecret !== "" &&
                    supportedGrantTypesArray[0] !== "" && supportedResponseTypesArray[0] !== ""){
                  axios({
                    method: "post",
                    url: url + "/api/services/oauth", //
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
                    window.location.replace(vm.s34);
                  });
                }else{
                  alert("لطفا قسمت های الزامی را پر کنید.");
                }
              }else if(vm.serviceType === "CAS"){
                axios({
                  method: "post",
                  url: url + "/api/services/cas", //
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
                  window.location.replace(vm.s34);
                });
              }
            });
          }else{
            vm.service.logo = null;
            if(vm.serviceType === "SAML"){
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
                      if(res.data != ""){
                        vm.service.metadataLocation = res.data;
                        axios({
                          method: "post",
                          url: url + "/api/services/saml", //
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
                          window.location.replace(vm.s34);
                        });
                      }
                    });
                  }
                }else if(vm.metaDataAddress){
                  if(document.getElementsByName("metadataLocation")[0].value != ""){
                    vm.service.metadataLocation = document.getElementsByName("metadataLocation")[0].value;
                    axios({
                      method: "post",
                      url: url + "/api/services/saml", //
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
                    })
                        .then((res) => {
                          window.location.replace(vm.s34);
                        });
                  }else{
                    alert("لطفا قسمت های الزامی را پر کنید.");
                  }
                }
              }else{
                alert("لطفا قسمت های الزامی را پر کنید.");
              }
            }else if(vm.serviceType === "Oauth2"){
              let supportedGrantTypesArray = document.getElementsByName("supportedGrantTypes")[0].getAttribute("value").split(", ");
              let supportedResponseTypesArray = document.getElementsByName("supportedResponseTypes")[0].getAttribute("value").split(", ");
              vm.service.clientId = document.getElementsByName("clientId")[0].value;
              vm.service.clientSecret = document.getElementsByName("clientSecret")[0].value;
              vm.service.supportedGrantTypes = [ "java.util.HashSet", supportedGrantTypesArray ];
              vm.service.supportedResponseTypes = [ "java.util.HashSet", supportedResponseTypesArray ];

              if(vm.service.clientId !== "" && vm.service.clientSecret !== "" &&
                  supportedGrantTypesArray[0] !== "" && supportedResponseTypesArray[0] !== ""){
                axios({
                  method: "post",
                  url: url + "/api/services/oauth", //
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
                  window.location.replace(vm.s34);
                });
              }else{
                alert("لطفا قسمت های الزامی را پر کنید.");
              }
            }else if(vm.serviceType === "CAS"){
              axios({
                method: "post",
                url: url + "/api/services/cas", //
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
                window.location.replace(vm.s34);
              });
            }
          }
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
      setServiceType: function () {
        if(this.serviceType == "CAS"){
          this.samls = false;
        }else if(this.serviceType == "Oauth2"){
          this.samls = true;
          let supportedGrantTypesContainer = document.getElementById("supportedGrantTypesContainer");
          new CheckboxDropdown(supportedGrantTypesContainer);
          let supportedResponseTypesContainer = document.getElementById("supportedResponseTypesContainer");
          new CheckboxDropdown(supportedResponseTypesContainer);
        }else{
          this.samls = true;
        }
      },
      generateClientSecret: function () {
        document.getElementsByName("clientSecret")[0].value = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
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
          this.s8 = "Create New Service";
          this.s9 = "fa fa-arrow-left";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s14 = "Basics";
          this.s15 = "Enable Service";
          this.s16 = "Service Type";
          this.s17 = "Service Name";
          this.s18 = "Service URL";
          this.s19 = "Service Farsi Name";
          this.s20 = "Reference Links";
          this.s21 = "Logo URL";
          this.s22 = "Information URL";
          this.s23 = "Privacy URL";
          this.s24 = "Contacts";
          this.s25 = "Name";
          this.s26 = "Email";
          this.s27 = "Phone";
          this.s28 = "Department";
          this.s29 = "Logout Options";
          this.s30 = "Logout URL";
          this.s31 = "Logout Type";
          this.s32 = "Submit";
          this.s35 = "Groups Access";
          this.s36 = "ServiceID";
          this.s41 = "Configs";
          this.s44 = "Access Strategy";
          this.s45 = "Allow SSO";
          this.s46 = "Unauthorized Redirect Url";
          this.s47 = " (Only English Letters And Numbers Are Allowed For Service Name)";
          this.s48 = "Basics";
          this.s49 = "Access Strategy";
          this.s50 = "Time Based Access";
          this.s51 = "Start Date";
          this.s52 = "Start Time";
          this.s53 = "End Date";
          this.s54 = "End Time";
          this.s55 = "Multi Factor Authentication";
          this.s56 = "MFA Enabled";
          this.s57 = " Example: 1399/05/01 ";
          this.s58 = " Example: 15:30 ";
          this.s59 = "Users Access";
          this.s60 = "Users List";
          this.s61 = "Search...";
          this.s62 = "Grant Access";
          this.s63 = "Revoke Access";
          this.s64 = "No User Found";
          this.s65 = "Search";
          this.s66 = "Authorized Users";
          this.s67 = "Remove";
          this.s68 = "List is Empty";
          this.s69 = "Banned Users";
          this.s70 = "Attribute Based Access";
          this.s71 = "Attribute Name";
          this.s72 = "Attribute Value";
          this.s73 = " (If Entering The Address, Mention http Or https)";
          this.s74 = "Address";
          this.s75 = "File";
          this.s76 = "Go Back";
          this.s77 = "Position";
          this.s78 = "Audits";
          this.s80 = "Remote Access";
          this.s81 = "Acceptable Response Codes";
          this.s82 = "Hardware Token";
          this.s83 = "Disabled";
          this.rolesText = "Roles";
          this.reportsText = "Reports";
          this.publicmessagesText = "Public Messages";
          this.ticketingText = "Ticketing";
          this.transcriptsText = "Access Reports";
          if(this.allGroupsHolderText == this.addAllGroupsText){
            this.allGroupsHolderText = "Select All";
          }else{
            this.allGroupsHolderText = "Unselect All";
          }
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
          this.otpText = "One Time Password (Google Authenticator)";
          this.serviceNotificationsText = "Service Notifications";
          this.apiAddressText = "API Address";
          this.apiKeyText = "API Key";
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
          this.smsText = "SMS";
        }else {
          window.localStorage.setItem("lang", "FA");
          this.margin = "margin-right: 30px;";
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
          this.s8 = "ایجاد سرویس جدید";
          this.s9 = "fa fa-arrow-right";
          this.s10 = "قوانین";
          this.s11 = "حریم خصوصی";
          this.s12 = "راهنما";
          this.s13 = "کاربران";
          this.s14 = "تنظیمات پایه";
          this.s15 = "فعال سازی سرویس";
          this.s16 = "نوع سرویس";
          this.s17 = "نام سرویس";
          this.s18 = "آدرس سرویس";
          this.s19 = "نام فارسی سرویس";
          this.s20 = "پیوندهای مرجع";
          this.s21 = "آدرس لوگو";
          this.s22 = "آدرس راهنما";
          this.s23 = "آدرس قوانین";
          this.s24 = "اطلاعات تماس";
          this.s25 = "نام";
          this.s26 = "ایمیل";
          this.s27 = "شماره تماس";
          this.s28 = "دپارتمان";
          this.s29 = "تنظیمات خروج";
          this.s30 = "آدرس خروج";
          this.s31 = "نوع خروج";
          this.s32 = "تایید";
          this.s35 = "دسترسی گروه ها";
          this.s36 = "شناسه سرویس";
          this.s41 = "پیکربندی";
          this.s44 = "استراتژی دسترسی";
          this.s45 = "فعال سازی SSO";
          this.s46 = "آدرس صفحه مقصد در صورت مجاز نبودن دسترسی";
          this.s47 =  " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)";
          this.s48 = "تنظیمات پایه";
          this.s49 = "استراتژی دسترسی";
          this.s50 = "دسترسی بر اساس زمان";
          this.s51 = "تاریخ شروع";
          this.s52 = "زمان شروع";
          this.s53 = "تاریخ پایان";
          this.s54 = "زمان پایان";
          this.s55 = "احراز هویت چند مرحله ای";
          this.s56 = "فعال سازی MFA";
          this.s57 = " مثال: 1399/05/01 ";
          this.s58 = " مثال: 20:30 ";
          this.s59 = "دسترسی کاربران";
          this.s60 = "لیست کاربران";
          this.s61 = "جستجو...";
          this.s62 = "اعطا دسترسی";
          this.s63 = "منع دسترسی";
          this.s64 = "کاربری یافت نشد";
          this.s65 = "جستجو کنید";
          this.s66 = "کاربران دارای دسترسی";
          this.s67 = "حذف";
          this.s68 = "لیست خالی است";
          this.s69 = "کاربران منع شده";
          this.s70 = "دسترسی بر اساس پارامتر";
          this.s71 = "نام پارامتر";
          this.s72 = "مقدار پارامتر";
          this.s73 = " (در صورت وارد کردن آدرس، http یا https ذکر شود)";
          this.s74 = "آدرس";
          this.s75 = "فایل";
          this.s76 = "بازگشت";
          this.s77 = "موقعیت";
          this.s78 = "ممیزی ها";
          this.s80 = "دسترسی از راه دور";
          this.s81 = "کد پاسخ های قابل قبول";
          this.s82 = "توکن سخت افزاری";
          this.s83 = "غیرفعال";
          this.rolesText = "نقش ها";
          this.reportsText = "گزارش ها";
          this.publicmessagesText = "اعلان های عمومی";
          this.ticketingText = "پشتیبانی";
          this.transcriptsText = "گزارش های دسترسی";
          if(this.allGroupsHolderText == this.addAllGroupsText){
            this.allGroupsHolderText = "انتخاب همه";
          }else{
            this.allGroupsHolderText = "لغو انتخاب همه";
          }
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
          this.otpText = "رمز یکبار مصرف (Google Authenticator)";
          this.serviceNotificationsText = "پیام های سرویس";
          this.apiAddressText = "آدرس API";
          this.apiKeyText = "کلید API";
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
          this.smsText = "رمز پیامکی (SMS)";
        }
      },
    },
    computed: {
      resultQuery(){
        if(this.searchQuery){
          let buffer = [];
          buffer = buffer.concat(this.userSearch.filter((item)=>{
            return this.searchQuery.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
          }));
          buffer = buffer.concat(this.userSearch.filter((item)=>{
            return this.searchQuery.split(' ').every(v => item.displayName.includes(v))
          }));
          let uniqueBuffer = [...new Set(buffer)];
          return uniqueBuffer;
        }else{
          return [];
        }
      },
      allowedResultQuery(){
        let buffer = [];
        buffer = buffer.concat(this.userAllowed.filter((item)=>{
          return this.searchQueryAllowedList.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
        }));
        buffer = buffer.concat(this.userAllowed.filter((item)=>{
          return this.searchQueryAllowedList.split(' ').every(v => item.displayName.includes(v))
        }));
        let uniqueBuffer = [...new Set(buffer)];
        return uniqueBuffer;
      },
      blockedResultQuery(){
        let buffer = [];
        buffer = buffer.concat(this.userBlocked.filter((item)=>{
          return this.searchQueryBlockedList.toLowerCase().split(' ').every(v => item.userId.toLowerCase().includes(v))
        }));
        buffer = buffer.concat(this.userBlocked.filter((item)=>{
          return this.searchQueryBlockedList.split(' ').every(v => item.displayName.includes(v))
        }));
        let uniqueBuffer = [...new Set(buffer)];
        return uniqueBuffer;
      },
    },
  })
})