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
            users: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            s0: "پارسو",
            s1: "",
            s2: "خروج",
            s3: "داشبورد",
            s4: "سرویس ها",
            s5: "گروه ها",
            s6: "رویداد ها",
            s7: "تنظیمات",
            s9: "fa fa-arrow-right",
            s10: "قوانین",
            s11: "حریم خصوصی",
            s12: "راهنما",
            s13: "کاربران",
            s14: "./dashboard",
            s15: "./services",
            s16: "./users",
            s17: "بازگشت",
            s18: "تایید",
            U0: "رمز",
            U1: "کاربران",
            U2: "شناسه",
            U3: "نام",
            U4: "نام خانوادگی",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تماس",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "اضافه کردن کاربر جدید",
            U13: "ویرایش",
        },
        created: function () {
            this.getUserInfo();
            this.refreshUsers();
            if(typeof this.$route.query.en !== 'undefined'){
              this.changeLang();
            }
        },
        methods: {
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/username")
                .then((res) => {
                  vm.username = res.data;
                  axios.get(url + "/idman/api/users/u/" + vm.username)
                  .then((res) => {
                    vm.userInfo = res.data;
                    vm.name = vm.userInfo.displayName;
                    vm.nameEN = vm.userInfo.firstName;
                    vm.s1 = vm.name;
                  });
                });
              },
              refreshUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/users")
                .then((res) => {
                  vm.users = res.data;
                });
              },
              showUsers: function () {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
              },
              updateUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + `/api/users/u/${id}`)
                .then((res) => {
                  for(i = 0; i < vm.users.length; ++i){
                    if(vm.users[i].userId == id){
                      vm.users[i].firstName = res.data.firstName;
                      vm.users[i].lastName = res.data.lastName;
                      vm.users[i].displayName = res.data.displayName;
                      vm.users[i].telephoneNumber = res.data.telephoneNumber;
                      vm.users[i].mail = res.data.mail;
                      vm.users[i].nid = res.data.nid;
                      vm.users[i].memberOf = res.data.memberOf;
                      vm.users[i].userPassword = res.data.userPassword;
                      vm.users[i].description = res.data.description;
                    }
                  }
                });
              },
              editUserS: function (id) {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = ""
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + `/api/users/u/${id}`)
                .then((res) => {
                  vm.editInfo = res.data;
                });
              },
              editUser: function (id) {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.put(url + `/api/users/u/${id}`, {
                  nid: vm.editInfo.nid,
                  firstName: vm.editInfo.firstName,
                  lastName: vm.editInfo.lastName,
                  displayName: vm.editInfo.displayName,
                  telephoneNumber: vm.editInfo.telephoneNumber,
                  mail: vm.editInfo.mail,
                  nid: vm.editInfo.nid,
                  memberOf: vm.editInfo.memberOf,
                  userPassword: vm.editInfo.userPassword,
                  description: vm.editInfo.description
                });
              },
              addUserS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
              },
              addUser: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.post(url + "/api/users", {
                  userId: vm.editInfo.userId,
                  firstName: vm.editInfo.firstName,
                  lastName: vm.editInfo.lastName,
                  displayName: vm.editInfo.displayName,
                  telephoneNumber: vm.editInfo.telephoneNumber,
                  mail: vm.editInfo.mail,
                  nid: vm.editInfo.nid,
                  memberOf: vm.editInfo.memberOf,
                  userPassword: vm.editInfo.userPassword,
                  description: vm.editInfo.description
                });
              },
              deleteUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.delete(url + `/api/users/u/${id}`)
                .then(() => {
                  vm.refreshUsers();
                });
              },
              deleteAllUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.delete(url + "/api/users/u/")
                .then(() => {
                  vm.refreshUsers();
                });
              },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
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
                    this.s7 = "Settings";
                    this.s9 = "fa fa-arrow-left";
                    this.s10 = "Rules";
                    this.s11 = "Privacy";
                    this.s12 = "Guide";
                    this.s13 = "Users";
                    this.s14 = "./dashboard?en";
                    this.s15 = "./services?en";
                    this.s16 = "./users?en";
                    this.s17 = "Go Back";
                    this.s18 = "Submit";
                    this.U0= "Password";
                    this.U1= "Users";
                    this.U2= "ID";
                    this.U3= "First Name";
                    this.U4= "Last Name";
                    this.U5= "FullName (In Persian)";
                    this.U6= "Phone";
                    this.U7= "Email";
                    this.U8= "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "Add New User";
                    this.U13 = "Edit";
                } else{
                    this.placeholder = "text-align: right;"
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
                    this.s7 = "تنظیمات";
                    this.s9 = "fa fa-arrow-right";
                    this.s10 = "قوانین";
                    this.s11 = "حریم خصوصی";
                    this.s12 = "راهنما";
                    this.s13 = "کاربران";
                    this.s14 = "./dashboard";
                    this.s15 = "./services";
                    this.s16 = "./users";
                    this.s17 = "بازگشت";
                    this.s18 = "تایید";
                    this.U0= "رمز";
                    this.U1= "کاربران";
                    this.U2= "شناسه";
                    this.U3= "نام";
                    this.U4= "نام خانوادگی";
                    this.U5= "نام کامل (به فارسی)";
                    this.U6= "شماره تماس";
                    this.U7= "ایمیل";
                    this.U8= "کد ملی";
                    this.U9= "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "اضافه کردن کاربر جدید";
                    this.U13 = "ویرایش";
                }
            }
        }
    })
})
