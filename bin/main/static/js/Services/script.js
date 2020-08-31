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
        message: "",
        editInfo: {},
        placeholder: "text-align: right;",
        margin: "margin-right: 30px;",
        lang: "EN",
        isRtl: true,
        groups: [],
        groupList: "",
        samls: "display: none;",
        editS: "display: none;",
        editS1: "display: none;",
        editS2: "display: none;",
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
        accessStrategy: {},
        requiredAttributes: {},
        ou: [],
        ouList: [],
        contacts: [],
        contactsList: [],
        contactsObj: {},
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
        s14: "سرویس جدید",
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
        s25: "تنطیمات پایه",
        s26: "فعال سازی سرویس",
        s27: "نوع سرویس",
        s28: "نام سرویس",
        s29: "آدرس سرویس",
        s30: "شناسه سرویس",
        s31: "معرفی سرویس",
        s32: "گروه ها",
        s33: "پیوندهای مرجع",
        s34: "آدرس لوگو",
        s35: "آدرس راهنما",
        s36: "آدرس قوانین",
        s37: "اطلاعات تماس",
        s38: "نام",
        s39: "ایمیل",
        s40: "شماره تماس",
        s41: "دپارتمان",
        s42: "تنطیمات خروج",
        s43: "آدرس خروج",
        s44: "نوع خروج",
        s45: "تایید",
        s46: "بازگشت",
        s47: "حذف همه",
        s48: "./groups",
        s49: "./settings",
        s50: "./privacy",
        s51: "پیکربندی",
        s52: "./configs",
        s53: "./events",
        s54: "استراتژی دسترسی",
        s55: "فعال سازی SSO",
        s56: "آدرس تغییر مسیر غیرمجاز",
        s57: " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)"
      },
      created: function () {
        this.getUserInfo();
        this.getUserPic();
        this.refreshServices();
        this.getGroups();
        if(typeof this.$route.query.en !== 'undefined'){
          this.changeLang()
        }
      },
      methods: {
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
        refreshServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/services") //
          .then((res) => {
            vm.services = res.data;
            vm.total = Math.ceil(vm.services.length / vm.recordsShownOnPage);
          });
        },
        showServices: function () {
          location.reload();
        },
        editServiceS: function (id) {
          document.getElementById("showS2").setAttribute("style", "display:none;");
          document.getElementById("showS3").setAttribute("style", "display:none;");
          document.getElementById("showS4").setAttribute("style", "display:none;");
          document.getElementById("showS5").setAttribute("style", "display:none;");
          document.getElementById("editS").setAttribute("style", "");
          document.getElementById("editS1").setAttribute("style", "");
          document.getElementById("editS2").setAttribute("style", "");
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

            document.getElementsByName("serviceId")[0].value = res.data.serviceId;

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

            if(typeof res.data.accessStrategy.requiredAttributes !== 'undefined'){
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

          if(document.getElementsByName('name')[0].value == "" || document.getElementsByName('serviceId')[0].value == "" ||
            document.getElementsByName('cName')[0].value == "" || document.getElementsByName('cEmail')[0].value == "" ||
            document.getElementsByName('description')[0].value == ""){
            alert("لطفا قسمت های الزامی را پر کنید.");
          }else{

            this.service.name = document.getElementsByName('name')[0].value;

            this.service.serviceId = document.getElementsByName('serviceId')[0].value;

            if(document.getElementsByName('description')[0].value != ""){
              this.service.description = document.getElementsByName('description')[0].value;
            }else{
              this.service.description = null;
            }

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

            if(document.getElementsByName('groups')[0].value != ""){
              this.ouList = document.getElementsByName('groups')[0].value.split(',');
              this.ou[0] = "java.util.HashSet";
              this.ou[1] = this.ouList;
              this.requiredAttributes.ou = this.ou;
            }else{
              this.requiredAttributes = {};
            }

            this.accessStrategy.requiredAttributes = this.requiredAttributes;
            
            
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

            axios({
              method: 'put',
              url: url + `/api/service/${id}`, //
              headers: {'Content-Type': 'application/json'},
              data: JSON.stringify({
                name: vm.service.name,
                serviceId: vm.service.serviceId,
                description: vm.service.description,
                logo: vm.service.logo,
                informationUrl: vm.service.informationUrl,
                privacyUrl: vm.service.privacyUrl,
                logoutType: vm.service.logoutType,
                logoutUrl: vm.service.logoutUrl,
                accessStrategy: vm.accessStrategy,
                contacts: vm.contacts
              })
            })
            .then((res) => {
              location.reload();
            });
          }
        },
        deleteService: function (id) {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.delete(url + `/api/services/${id}`) //
          .then(() => {
            location.reload();
          });
        },
        deleteAllServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.delete(url + "/api/services") //
          .then(() => {
            location.reload();
          });
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
            this.s7 = "Profile";
            this.s8 = "Connected Services";
            this.s9 = "Services";
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
            this.s31 = "Description";
            this.s32 = "Groups";
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
            this.s47 = "Remove All";
            this.s48 = "./groups?en";
            this.s49 = "./settings?en";
            this.s50 = "./privacy?en";
            this.s51 = "Configs";
            this.s52 = "./configs?en";
            this.s53 = "./events?en";
            this.s54 = "Access Strategy";
            this.s55 = "Allow SSO";
            this.s56 = "Unauthorized Redirect Url";
            this.s57 =  " (Only English Letters And Numbers Are Allowed For Service Name)";
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
              this.s7 = "پروفایل";
              this.s8 = "سرویس های متصل";
              this.s9 = "سرویس ها";
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
              this.s20 = "شناسه";
              this.s21 = "نام";
              this.s22 = "آدرس";
              this.s23 = "ویرایش";
              this.s24 = "حذف";
              this.s25 = "تنطیمات پایه";
              this.s26 = "فعال سازی سرویس";
              this.s27 = "نوع سرویس";
              this.s28 = "نام سرویس";
              this.s29 = "آدرس سرویس";
              this.s30 = "شناسه سرویس";
              this.s31 = "معرفی سرویس";
              this.s32 = "گروه ها";
              this.s33 = "پیوندهای مرجع";
              this.s34 = "آدرس لوگو";
              this.s35 = "آدرس راهنما";
              this.s36 = "آدرس قوانین";
              this.s37 = "اطلاعات تماس";
              this.s38 = "نام";
              this.s39 = "ایمیل";
              this.s40 = "شماره تماس";
              this.s41 = "دپارتمان";
              this.s42 = "تنطیمات خروج";
              this.s43 = "آدرس خروج";
              this.s44 = "نوع خروج";
              this.s45 = "تایید";
              this.s46 = "بازگشت";
              this.s47 = "حذف همه";
              this.s48 = "./groups";
              this.s49 = "./settings";
              this.s50 = "./privacy";
              this.s51 = "پیکربندی";
              this.s52 = "./configs";
              this.s53 = "./events";
              this.s54 = "استراتژی دسترسی";
              this.s55 = "فعال سازی SSO";
              this.s56 = "آدرس تغییر مسیر غیرمجاز";
              this.s57 =  " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)";
          }
        },
        saml: function () {
          this.samls = "";
          this.s18 = "Entity ID";
          },
        cas: function () {
          this.samls = "display: none;";
          if(this.lang == "EN"){
            this.s18 = "آدرس سرویس";
          }else{
            this.s18 = "Service URL";
          }
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
        }
      }
    });
  })