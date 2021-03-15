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
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
      router,
      el: '#app',
      data: {
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
        samls: true,
        editS: "display: none;",
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
        metaDataAddress: true,
        metaDataFile: false,
        allIsSelected: false,
        isListEmpty: false,
        s0: "پارسو",
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
      },
      created: function () {
        this.getUserInfo();
        this.getUserPic();
        this.refreshServices();
        this.getGroups();
        this.getUsersList();
        if(typeof this.$route.query.en !== 'undefined'){
          this.changeLang()
        }
      },
      methods: {
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
        getUsersList: function (){
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/users")
          .then((res) => {
            vm.userSearch = res.data;
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
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
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

            if(typeof res.data.metadataLocation !== 'undefined'){
              vm.samls = true;
              document.getElementsByName("metadataLocation")[0].value = res.data.metadataLocation;
            }else{
              vm.samls = false;
            }

            if(typeof res.data.description !== 'undefined'){
              document.getElementsByName("description")[0].value = res.data.description;
            }

            if(typeof res.data.extraInfo !== 'undefined'){
              if(typeof res.data.extraInfo.url !== 'undefined'){
                document.getElementsByName("url")[0].value = res.data.extraInfo.url;
              }
            }

            if(res.data.accessStrategy.ssoEnabled){
              document.getElementsByName("ssoEnabled")[0].checked = true;
            }else{
              document.getElementsByName("ssoEnabled")[0].checked = false;
            }

            if(typeof res.data.accessStrategy.unauthorizedRedirectUrl !== 'undefined'){
              document.getElementsByName("unauthorizedRedirectUrl")[0].value = res.data.accessStrategy.unauthorizedRedirectUrl;
            }

            if(typeof res.data.accessStrategy.endpointUrl !== 'undefined'){
              document.getElementsByName("endpointUrl")[0].value = res.data.accessStrategy.endpointUrl;
            }

            if(typeof res.data.accessStrategy.acceptableResponseCodes !== 'undefined'){
              document.getElementsByName("acceptableResponseCodes")[0].value = res.data.accessStrategy.acceptableResponseCodes;
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

            if(typeof res.data.accessStrategy.rejectedAttributes !== 'undefined'){
              if(typeof res.data.accessStrategy.rejectedAttributes.uid !== 'undefined'){
                let countA = 0;
                for(let i = 0; i < this.userSearch.length; ++i){
                  for(let j = 0; j < res.data.accessStrategy.rejectedAttributes.uid[1].length; ++j){
                    if(this.userSearch[i].userId == res.data.accessStrategy.rejectedAttributes.uid[1][j]){
                      this.blockUserAccess(this.userSearch[i]);
                      countA = countA + 1;
                      break;
                    }
                  }
                  if(countA == res.data.accessStrategy.rejectedAttributes.uid[1].length){
                    break;
                  }
                }
              }
            }

            if(typeof res.data.accessStrategy.requiredAttributes !== 'undefined'){
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

              if(typeof res.data.accessStrategy.requiredAttributes.uid !== 'undefined'){
                let countB = 0;
                for(let i = 0; i < this.userSearch.length; ++i){
                  for(let j = 0; j < res.data.accessStrategy.requiredAttributes.uid[1].length; ++j){
                    if(this.userSearch[i].userId == res.data.accessStrategy.requiredAttributes.uid[1][j]){
                      this.allowUserAccess(this.userSearch[i]);
                      countB = countB + 1;
                      break;
                    }
                  }
                  if(countB == res.data.accessStrategy.requiredAttributes.uid[1].length){
                    break;
                  }
                }
              }

              if(typeof res.data.accessStrategy.requiredAttributes.ou !== 'undefined'){
                for(let i = 0; i < res.data.accessStrategy.requiredAttributes.ou[1].length; ++i){
                  document.getElementById("groupNameId" + res.data.accessStrategy.requiredAttributes.ou[1][i]).checked = true;
                  if(vm.groupList === ""){
                    vm.groupList += res.data.accessStrategy.requiredAttributes.ou[1][i];
                  }else{
                    vm.groupList += ',' + res.data.accessStrategy.requiredAttributes.ou[1][i];
                  }
                }
              }
            }
          });
        },
        editService: function (id) {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;

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

            if(document.getElementsByName('logo')[0].value != ""){
              this.service.logo = document.getElementsByName('logo')[0].value;
            }else{
              this.service.logo = null;
            }

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

            if(document.getElementsByName('endpointUrl')[0].value != ""){
              this.accessStrategy.endpointUrl = document.getElementsByName('endpointUrl')[0].value;
            }else{
              this.accessStrategy.endpointUrl = null;
            }
  
            if(document.getElementsByName('acceptableResponseCodes')[0].value != ""){
              this.accessStrategy.acceptableResponseCodes = document.getElementsByName('acceptableResponseCodes')[0].value;
            }else{
              if(document.getElementsByName('endpointUrl')[0].value != ""){
                this.accessStrategy.acceptableResponseCodes = "200";
              }else{
                this.accessStrategy.acceptableResponseCodes = null;
              }
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

            if(this.samls){
              if(this.metaDataAddress || this.metaDataFile){
                if(this.metaDataFile){
                  let file = document.querySelector('#file');
                  if(!file.files[0]){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                  }else{
                    let bodyFormData = new FormData();
                    bodyFormData.append("file", file.files[0]);
                    axios({
                      method: 'post',
                      url: url + "/api/services/metadata",  //
                      headers: {'Content-Type': 'multipart/form-data'},
                      data: bodyFormData,
                    }).then((res) => {
                      if(res.data != ""){
                        this.service.metadataLocation = res.data;

                        axios({
                          method: 'put',
                          url: url + `/api/service/${id}/saml`, //
                          headers: {'Content-Type': 'application/json'},
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
                            contacts: vm.contacts
                          }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                          location.reload();
                        });
                      }
                    });
                  }
                }
  
                if(this.metaDataAddress){
                  if(document.getElementsByName('metadataLocation')[0].value != ""){
                    this.service.metadataLocation = document.getElementsByName('metadataLocation')[0].value;

                    axios({
                      method: 'put',
                      url: url + `/api/service/${id}/saml`, //
                      headers: {'Content-Type': 'application/json'},
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
                        contacts: vm.contacts
                      }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                      location.reload();
                    });
                  }else{
                    alert("لطفا قسمت های الزامی را پر کنید.");
                  }
                }                
              }else{
                alert("لطفا قسمت های الزامی را پر کنید.");
              }
            }else{

              axios({
                method: 'put',
                url: url + `/api/service/${id}/cas`, //
                headers: {'Content-Type': 'application/json'},
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
                  contacts: vm.contacts
                }).replace(/\\\\/g, "\\")
              })
              .then((res) => {
                location.reload();
              });
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
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port; //
          var vm = this;
          axios.get(url + "/api/groups")
          .then((res) => {
            vm.groups = res.data;
          });
        },
        addGroup: function (n) {
          n = n.split("'").join("");
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
        serviceNameValidate ($event) {
          let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
          if (keyCode < 48 || keyCode > 122) {
             $event.preventDefault();
          }
        },
        FaNumToEnNum: function (str) {
          let s = str.split("");
          let sEn = "";
          for(i = 0; i < s.length; ++i){
            if(s[i] == '۰'){
              sEn = sEn + '0';
            }else if(s[i] == '۱'){
              sEn = sEn + '1';
            }else if(s[i] == '۲'){
              sEn = sEn + '2';
            }else if(s[i] == '۳'){
              sEn = sEn + '3';
            }else if(s[i] == '۴'){
              sEn = sEn + '4';
            }else if(s[i] == '۵'){
              sEn = sEn + '5';
            }else if(s[i] == '۶'){
              sEn = sEn + '6';
            }else if(s[i] == '۷'){
              sEn = sEn + '7';
            }else if(s[i] == '۸'){
              sEn = sEn + '8';
            }else if(s[i] == '۹'){
              sEn = sEn + '9';
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
        changeLang: function () {
          if(this.lang == "EN"){
            this.margin = "margin-left: 30px;";
            this.margin1 = "mr-1";
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
            this.s8 = "Connected Services";
            this.s9 = "Services";
            this.s10 = "Rules";
            this.s11 = "Privacy";
            this.s12 = "Guide";
            this.s13 = "Create New Service";
            this.s14 = "New";
            this.s15 = "Users";
            this.s16 = "./dashboard?en";
            this.s17 = "./services?en";
            this.s18 = "./createservice?en";
            this.s19 = "./users?en";
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
            this.s48 = "./groups?en";
            this.s49 = "./profile?en";
            this.s50 = "./privacy?en";
            this.s51 = "Configs";
            this.s52 = "./configs?en";
            this.s53 = "./events?en";
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
            this.s95 = "/audits?en";
            this.s96 = "Remote Access";
            this.s97 = "Acceptable Response Codes";
            this.s98 = "Edit Service";
            this.s99 = "Hardware Token";
            this.s100 = "Disabled";
            this.s101 = "No Service Found";
          } else{
              this.margin = "margin-right: 30px;";
              this.margin1 = "ml-1";
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
              this.s8 = "سرویس های متصل";
              this.s9 = "سرویس ها";
              this.s10 = "قوانین";
              this.s11 = "حریم خصوصی";
              this.s12 = "راهنما";
              this.s13 = "ایجاد سرویس جدید";
              this.s14 = "جدید";
              this.s15 = "کاربران";
              this.s16 = "./dashboard";
              this.s17 = "./services";
              this.s18 = "./createservice";
              this.s19 = "./users";
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
              this.s48 = "./groups";
              this.s49 = "./profile";
              this.s50 = "./privacy";
              this.s51 = "پیکربندی";
              this.s52 = "./configs";
              this.s53 = "./events";
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
              this.s95 = "/audits";
              this.s96 = "دسترسی از راه دور";
              this.s97 = "کد پاسخ های قابل قبول";
              this.s98 = "ویرایش سرویس";
              this.s99 = "توکن سخت افزاری";
              this.s100 = "غیرفعال";
              this.s101 = "سرویسی یافت نشد";
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
        }
      }
    });
  })