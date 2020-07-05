function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

function BothFieldsIdentical (id1 , id2) {
    var one = document.getElementById(id1).value;
    var another = document.getElementById(id2).value;
    if(one == another) { return true; }
    alert("Both fields for password must be identical.");
    return false;
}

function validatePassword(password) {

    // Do not show anything when the length of password is zero.
    if (password.length === 0) {
        document.getElementById("msg").innerHTML = "";
        return;
    }
    // Create an array and push all possible values that you want in password
    var matchedCase = new Array();
    matchedCase.push("[$@$!%*#?&]"); // Special Charector
    matchedCase.push("[A-Z]");      // Uppercase Alpabates
    matchedCase.push("[0-9]");      // Numbers
    matchedCase.push("[a-z]");     // Lowercase Alphabates

    // Check the conditions
    var ctr = 0;
    for (var i = 0; i < matchedCase.length; i++) {
        if (new RegExp(matchedCase[i]).test(password)) {
            ctr++;
        }
    }

    var value = document.getElementById('ediInfo.userPasswordUpdate').value;
    if (value.length > 7) {
        console.log("No")
    }

    console.log(ctr)
    // Display it
    var color = "";
    var strength = "";
    switch (ctr) {
        case 0:
        case 1:
        case 2:
            strength = "خیلی ضعیف";
            color = "red";
            break;
        case 3:
            strength = "متوسط";
            color = "orange";
            break;
        case 4:
            strength = "قوی";
            color = "lightgreen";
            break;
        case 5:
            strength = "خیلی قوی";
            color = "darkseagreen";
            break;
    }
    document.getElementById("msg1").innerHTML = strength;
    document.getElementById("msg1").style.color = color;
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
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
        router,
        el: '#app',
        data: {
            recordsShownOnPage: 2,
            currentSort: "id",
            currentSortDir: "asc",
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            users: [],
            usersPage: [],
            message: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
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
            margin1: "ml-1",
            margin5: "ml-5",
            userPicture: "images/PlaceholderUser.png",
            s0: "پارسو",
            s1: "",
            s2: "خروج",
            s3: "بازنشانی رمز عبور",
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
            s19: "ایمیل",
            s20: "داشبورد",
            s21: "./groups",
            s22: "./settings",
            s23: "فایلی انتخاب نشده است.",
            s24: "از فرمت فایل انتخابی پشتیبانی نمی شود.",
            s25: "سایز فایل انتخابی بیش از 100M می باشد.",
            s26: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s27: "آیا از افزودن این کاربر اطمینان دارید؟",
            s28: "آیا از حذف این کاربر اطمینان دارید؟",
            s29: "آیا از حذف تمامی کاربران اطمینان دارید؟",
            s30: "./privacy",
            U0: "رمز عبور",
            U1: "کاربران",
            U2: "شناسه",
            U3: "نام (به انگلیسی)",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام کامل (به فارسی)",
            U6: "شماره تماس",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "اضافه کردن کاربر جدید",
            U13: "ویرایش",
            U14: "گروههای عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف تمامی کاربران",
            U18: "وارد کردن کاربران با فایل",
            U19: "ذخیره سازی داده ها در فایل",
            U20: "وارد کردن کاربران با فایل",
            U21: "بارگزاری",
            U22: "بازنشانی رمز عبور",
            h1: "ترکیبی از حروف و اعداد. مثال: ali123",
            p1: "خیلی ضعیف",
            p2: "متوسط",
            p3: "قوی",
            p4: "- باید حداقل 8 کاراکتر باشد",
            p5: "- باید ترکیبی از حرف و عدد باشد",
            p6: "- باید شامل حروف بزرگ و کوچک باشد",
            p7:"رمز در نظر گرفته شده باید:"

        },
        created: function () {
            this.getUserInfo();
            this.getUserPic();
            this.refreshUsers();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            selectedFile() {
                const file = this.$refs.file.files[0];
                var re = /(\.xls|\.xlsx|\.csv)$/i;

                if (!file) {
                    alert(this.s23);
                    return;
                }else{
                    var fup = document.getElementById('file');
                    var fileName = fup.value;
                    if(!re.exec(fileName)){
                        alert(this.s24);
                        return;
                    }else{
                        if(file.size > 1024 * 1024 * 100) {
                            alert(this.s25);
                            return;
                        }else{
                            document.getElementById("myForm").submit();
                        }
                    }
                }
            },
            sort:function(s) {
                console.log("sort");
                if(s === this.currentSort) {
                  this.currentSortDir = this.currentSortDir === 'asc'?'desc':'asc';
                }
                this.currentSort = s;
            },
            getUserInfo: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/api/user") // 
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
                axios.get(url + "/idman/api/user/photo") // 
                    .then((res) => {
                        if(res.data == null){
                            vm.userPicture = "images/PlaceholderUser.png";
                        }else{
                            vm.userPicture = /* url + */ "/api/user/photo"; // /idman
                        }
                    });
            },
            refreshUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/api/users") // 
                    .then((res) => {
                        vm.users = res.data;
                        vm.total = Math.ceil(vm.users.length / vm.recordsShownOnPage);
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
                axios.get(url + `/idman/api/users/u/${id}`) // 
                    .then((res) => {
                        for(i = 0; i < vm.users.length; ++i){
                            if(vm.users[i].userId == id){
                                vm.users[i].firstName = res.data.firstName;
                                vm.users[i].lastName = res.data.lastName;
                                vm.users[i].displayName = res.data.displayName;
                                vm.users[i].telephoneNumber = res.data.telephoneNumber;
                                vm.users[i].mail = res.data.mail;
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
                var res;
                axios.get(url + `/idman/api/users/u/${id}`) // 
                    .then((res) => {
                        vm.editInfo = res.data;
                        vm.editInfo.password = res.data.userPassword;
                        vm.editInfo.phone = res.data.telephoneNumber;
                        populate(res.data.memberOf);

                    });

                function populate(checkedGroups) {

                    axios.get(url + `/idman/api/groups`) // 
                        .then((res) => {
                                populateTwo(res.data, checkedGroups)
                            }
                        );
                    function populateTwo(allGroups, checkedGroups) {
                        for (var i = 0; i < allGroups.length; i++) {
                            let iDiv = document.createElement('div');
                            iDiv.id = 'block' + i;
                            iDiv.className = 'block';
                            document.getElementById('lsGroupsUpdate').appendChild(iDiv);
                            var v = document.createElement('input');
                            v.setAttribute("id", "checkbox" + i);
                            v.setAttribute("class","groupsCheckBox");
                            v.type = "checkbox";
                            console.log(v.getAttribute("id", "checkbox" + i));
                            v.value = allGroups[i].name;
                            if (checkedGroups!=null) {
                                for (var j = 0; j < checkedGroups.length; j++) {
                                    if ((allGroups[i].name).localeCompare(checkedGroups[j]) == 0) {
                                        v.checked = true;
                                    }
                                }
                            }
                            let l = document.createElement('label');
                            l.setAttribute("for", v.value);
                            l.innerHTML = v.value;
                            document.getElementById('lsGroupsUpdate').appendChild(v);
                            document.getElementById('lsGroupsUpdate').appendChild(l);
                            let innerDiv = document.createElement('div');
                            innerDiv.className = 'block-2';
                            iDiv.appendChild(innerDiv);
                        }
                    };
                }


            },
            editUser: function (id) {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm(this.s26);

                var checkedValue = [];
                var inputElements = document.getElementsByClassName('groupsCheckBox');


                for(var i=0; i<inputElements.length; i++){
                    console.log(inputElements[i].value)
                    console.log(inputElements[i].checked)
                    if(inputElements[i].checked ==true){
                        checkedValue.push(inputElements[i].value);
                    }
                }

                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/idman/api/users/u/' + id,  // 
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            userId: id,
                            firstName: document.getElementById('editInfo.firstNameUpdate').value,
                            lastName: document.getElementById('editInfo.lastNameUpdate').value,
                            displayName: document.getElementById('editInfo.displayNameUpdate').value,
                            telephoneNumber: document.getElementById('editInfo.phoneUpdate').value,
                            memberOf: checkedValue,
                            mail: document.getElementById('editInfo.mailUpdate').value,
                            userPassword: document.getElementById('editInfo.passwordRetypeUpdate').value,
                            description: document.getElementById('editInfo.descriptionUpdate').value,
                        }),
                    },);
                }

            },
            exportUsers: function(){
                url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;

                axios.get(url + "/idman/api/users/full") // 
                    .then((res) => {
                        data = res.data;
                        var opts = [{sheetid:'Users',header:true}]
                        var result = alasql('SELECT * INTO XLSX("users.xlsx",?) FROM ?',
                            [opts,[data]]);
                    });

            },
            addUserS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;

                axios.get(url + `/idman/api/groups`) // 
                    .then((res) => {
                            populate(res.data);
                        }
                    );
                function populate(allGroups) {
                    console.log(allGroups)
                    for (var i = 0; i < allGroups.length; i++) {
                        let iDiv = document.createElement('div');
                        iDiv.id = 'block' + i;
                        iDiv.className = 'block';
                        document.getElementById('lsGroupsAdd').appendChild(iDiv);
                        var v = document.createElement('input');
                        v.setAttribute("id", "checkboxaddpart" + i);
                        v.setAttribute("class" , "groupsCheckBoxCreate")
                        v.type = "checkbox";
                        v.value = allGroups[i].name;
                        let l = document.createElement('label');
                        l.setAttribute("for", v.value);
                        l.innerHTML = v.value;
                        document.getElementById('lsGroupsAdd').appendChild(v);
                        document.getElementById('lsGroupsAdd').appendChild(l);
                        let innerDiv = document.createElement('div');
                        innerDiv.className = 'block-2';
                        iDiv.appendChild(innerDiv);
                    }
                };
            },
            sendResetEmail(userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/idman/api/users/u/" + userId) // 
                    .then((res) => {
                        axios.get(url + "/idman/api/public/sendMail/" + res.data.mail) // 
                            .then((resFinal) => {
                            });
                    })
            },
            checkedBoxes:function() {
                var checkedValue = [];
                var inputElements = document.getElementsByClassName('groupsCheckBox');
                for(var i=0; inputElements.length; i++){
                    console.log(inputElements[i].value);
                    if(inputElements[i].checked){
                        console.log(inputElements[i].value);
                        checkedValue.push(inputElements[i].value);
                    }
                }
                return checkedValue;
            },
            addUser: function (id1,id2) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var checkedValue = [];
                var inputElements = document.getElementsByClassName('groupsCheckBoxCreate');
                var check = confirm(this.s27);

                console.log(inputElements.length);

                for(var i=0; i<inputElements.length; i++){
                    if(inputElements[i].checked ==true){
                        checkedValue.push(inputElements[i].value);
                    }
                }

                if(check==true) {
                    axios({
                        method: 'post',
                        url: url + "/idman/api/users",  // 
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                                userId: document.getElementById('editInfo.userIdCreate').value,
                                firstName: document.getElementById('editInfo.firstNameCreate').value,
                                lastName: document.getElementById('editInfo.lastNameCreate').value,
                                displayName: document.getElementById('editInfo.displayNameCreate').value,
                                telephoneNumber: document.getElementById('editInfo.phoneCreate').value,
                                memberOf: checkedValue,
                                mail: document.getElementById('editInfo.mailCreate').value,
                                userPassword: document.getElementById('editInfo.passwordRetypeCreate').value,
                                description: document.getElementById('editInfo.descriptionCreate').value,

                            }
                        ),
                    },);
                }
            },
            deleteUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s28);
                if (check == true) {
                    axios.delete(url + `/idman/api/users/u/${id}`) // 
                        .then(() => {
                            vm.refreshUsers();
                        });
                }
            },
            deleteAllUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s29);
                if (check == true) {
                    axios.delete(url + `/idman/api/users`) // 
                        .then(() => {
                            vm.refreshUsers();
                        });
                }

            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
                    this.margin5 = "mr-5";
                    this.s0 = "Parsso";
                    this.s1 = this.nameEN;
                    this.s2 = "Exit";
                    this.s3 = "Reset Password";
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
                    this.s19 = "Email";
                    this.s20 = "Dashboard";
                    this.s21 = "./groups?en";
                    this.s22 = "./settings?en";
                    this.s23 = "No File Chosen.";
                    this.s24 = "File extension not supported!";
                    this.s25 = "File too big (> 100MB)";
                    this.s26 = "Are You Sure You Want To Edit?";
                    this.s27 = "Are You Sure You Want To Add This User?";
                    this.s28 = "Are You Sure You Want To Delete?";
                    this.s29 = "Are You Sure You Want To Delete All Users?";
                    this.s30 = "./privacy?en";
                    this.U0= "Password";
                    this.U1= "Users";
                    this.U2= "ID";
                    this.U3= "First Name (In English)";
                    this.U4= "Last Name (In English)";
                    this.U5= "FullName (In Persian)";
                    this.U6= "Phone";
                    this.U7= "Email";
                    this.U8= "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "Add New User";
                    this.U13 = "Edit";
                    this.U17 = "Remove all user";
                    this.U18= "Export Users to a file";
                    this.U19= "Save in file";
                    this.U20= "Import users using file";
                    this.U21= "Upload";
                    this.U22= "Password Reset";
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
                    this.margin5 = "ml-5";
                    this.s0 = "پارسو";
                    this.s1 = this.name;
                    this.s2 = "خروج";
                    this.s3 = "بازنشانی رمز عبور";
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
                    this.s19 = "ایمیل";
                    this.s20 = "داشبورد";
                    this.s21 = "./groups";
                    this.s22 = "./settings";
                    this.s23 = "فایلی انتخاب نشده است.";
                    this.s24 = "از فرمت فایل انتخابی پشتیبانی نمی شود.";
                    this.s25 = "سایز فایل انتخابی بیش از 100M می باشد.";
                    this.s26 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s27 = "آیا از افزودن این کاربر اطمینان دارید؟";
                    this.s28 = "آیا از حذف این کاربر اطمینان دارید؟";
                    this.s29 = "آیا از حذف تمامی کاربران اطمینان دارید؟";
                    this.s30 = "./privacy";
                    this.U0= "رمز";
                    this.U1= "کاربران";
                    this.U2= "شناسه";
                    this.U3= "نام (به انگلیسی)";
                    this.U4= "نام خانوادگی (به انگلیسی)";
                    this.U5= "نام کامل (به فارسی)";
                    this.U6= "شماره تماس";
                    this.U7= "ایمیل";
                    this.U8= "کد ملی";
                    this.U9= "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "اضافه کردن کاربر جدید";
                    this.U13 = "ویرایش";
                    this.U17 = "حذف تمامی کاربران";
                    this.U18= "وارد کردن کاربران با فایل";
                    this.U19= "ذخیره سازی داده ها در فایل";
                    this.U20= "وارد کردن کاربران با فایل";
                    this.U21= "بارگزاری";
                    this.U22= "بازنشانی رمز عبور";
                }
            }
        },
        computed:{
            sortedUsers:function() {
                this.usersPage = [];
                for(let i = 0; i < this.recordsShownOnPage; ++i){
                    if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.users.length - 1){
                        this.usersPage[i] = this.users[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
                    }
                }
                return this.usersPage;
            }
        }
    });
})
