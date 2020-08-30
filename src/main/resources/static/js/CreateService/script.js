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
  new Vue({
    router,
    el: '#app',
    data: {
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      groups: [],
      groupList: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      samls: "display: none;",
      userPicture: "images/PlaceholderUser.png",
      accessStrategy: {},
      requiredAttributes: {},
      ou: [],
      ouList: [],
      contacts: [],
      contactsList: [],
      contactsObj: {},
      service: {},
      s0: "پارسو",
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
      s14: "تنطیمات پایه",
      s15: "فعال سازی سرویس",
      s16: "نوع سرویس",
      s17: "نام سرویس",
      s18: "آدرس سرویس",
      s19: "معرفی سرویس",
      s20: "پیوندهای مرجع",
      s21: "آدرس لوگو",
      s22: "آدرس راهنما",
      s23: "آدرس قوانین",
      s24: "اطلاعات تماس",
      s25: "نام",
      s26: "ایمیل",
      s27: "شماره تماس",
      s28: "دپارتمان",
      s29: "تنطیمات خروج",
      s30: "آدرس خروج",
      s31: "نوع خروج",
      s32: "تایید",
      s33: "./dashboard",
      s34: "./services",
      s35: "گروه ها",
      s36: "شناسه سرویس",
      s37: "./users",
      s38: "./groups",
      s39: "./settings",
      s40: "./privacy",
      s41: "پیکربندی",
      s42: "./configs",
      s43: "./events",
      s44: "استراتژی دسترسی",
      s45: "فعال سازی SSO",
      s46: "آدرس تغییر مسیر غیرمجاز",
      s47: " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)"
    },
    created: function () {
      this.getUserInfo();
      this.getUserPic();
      this.getGroups();
      if(typeof this.$route.query.en !== 'undefined'){
        this.changeLang();
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
      getGroups: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
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
      addService: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;

        if(document.getElementsByName('name')[0].value == "" || document.getElementsByName('serviceId')[0].value == "" ||
        document.getElementsByName('cName')[0].value == "" || document.getElementsByName('cEmail')[0].value == ""){
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
              method: 'post',
              url: url + "/api/services", //
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
            window.location.replace(url + "/services");
          });
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
          this.s19 = "Description";
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
          this.s33 = "./dashboard?en";
          this.s34 = "./services?en";
          this.s35 = "Groups";
          this.s36 = "ServiceID";
          this.s37 = "./users?en";
          this.s38 = "./groups?en";
          this.s39 = "./settings?en";
          this.s40 = "./privacy?en";
          this.s41 = "Configs";
          this.s42 = "./configs?en";
          this.s43 = "./events?en";
          this.s44 = "Access Strategy";
          this.s45 = "Allow SSO";
          this.s46 = "Unauthorized Redirect Url";
          this.s47 =  " (Only English Letters And Numbers Are Allowed For Service Name)";
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
            this.s8 = "ایجاد سرویس جدید";
            this.s9 = "fa fa-arrow-right";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "کاربران";
            this.s14 = "تنطیمات پایه";
            this.s15 = "فعال سازی سرویس";
            this.s16 = "نوع سرویس";
            this.s17 = "نام سرویس";
            this.s18 = "آدرس سرویس";
            this.s19 = "معرفی سرویس";
            this.s20 = "پیوندهای مرجع";
            this.s21 = "آدرس لوگو";
            this.s22 = "آدرس راهنما";
            this.s23 = "آدرس قوانین";
            this.s24 = "اطلاعات تماس";
            this.s25 = "نام";
            this.s26 = "ایمیل";
            this.s27 = "شماره تماس";
            this.s28 = "دپارتمان";
            this.s29 = "تنطیمات خروج";
            this.s30 = "آدرس خروج";
            this.s31 = "نوع خروج";
            this.s32 = "تایید";
            this.s33 = "./dashboard";
            this.s34 = "./services";
            this.s35 = "گروه ها";
            this.s36 = "شناسه سرویس";
            this.s37 = "./users";
            this.s38 = "./groups";
            this.s39 = "./settings";
            this.s40 = "./privacy";
            this.s41 = "پیکربندی";
            this.s42 = "./configs";
            this.s43 = "./events";
            this.s44 = "استراتژی دسترسی";
            this.s45 = "فعال سازی SSO";
            this.s46 = "آدرس تغییر مسیر غیرمجاز";
            this.s47 =  " (برای نام سرویس تنها حروف انگلیسی و اعداد مجاز می باشد)";
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
    }
  })
})