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
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
        router,
        el: '#app',
        data: {
            recordsShownOnPage: 20,
            currentSort: "id",
            currentSortDir: "asc",
            userInfo: [],
            email: "",
            username: "",
            name: "",
            nameEN: "",
            menuSA: false,
            users: [],
            usersPage: [],
            usersConflictList: [],
            usernameList: [],
            usersCorrectList: [],
            usersCorrectUserIdList: [],
            message: "",
            groups: [],
            groupListCreate: "",
            groupListUpdate: "",
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            editS: "display:none",
            addS: "display:none",
            showS: "",
            importConflictS: "display:none",
            currentPage: 1,
            total: 1,
            ImportedFile: '',
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
            margin3: "ml-3",
            margin5: "ml-5",
            padding0Left: "padding-left: 0rem;",
            padding0Right: "padding-right: 0rem;",
            userPicture: "images/PlaceholderUser.png",
            activeItem: "info",
            eye: "right: 1%;",
            font: "font-size: 0.74em; text-align: right;",
            rules: [
                { message:"حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ", regex:/[a-zA-Z]+/, fa:false},
                { message:"حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ",  regex:/[!@#\$%\^\&*\)\(+=\[\]._-]+/, fa:true},
				{ message:"حداقل ۸ کاراکتر باشد. ", regex:/.{8,}/, fa:false},
				{ message:"حداقل شامل یک عدد باشد. ", regex:/[0-9]+/, fa:false}
            ],
            show: false,
            showR: false,
            showC: false,
            has_number: false,
            has_lowerUPPERcase: false,
            has_specialchar: false,
            has_char: false,
            password: "",
            checkPassword: "",
			passwordVisible: true,
            submitted: false,
            userFound : false,
            showCreate: false,
            showRCreate: false,
            showCCreate: false,
            has_numberCreate: false,
            has_lowerUPPERcaseCreate: false,
            has_specialcharCreate: false,
            has_charCreate: false,
            passwordCreate: "",
            checkPasswordCreate: "",
			passwordVisibleCreate: true,
            submittedCreate: false,
            userStatus: "",
            resetPassEmailSent: false,
            s0: "پارسو",
            s1: "",
            s2: "خروج",
            s3: "بازنشانی رمز عبور",
            s4: "سرویس ها",
            s5: "گروه ها",
            s6: "رویداد ها",
            s7: "پروفایل",
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
            s31: "پیکربندی",
            s32: "./configs",
            s33: "رفع تناقض وارد کردن کاربران جدید",
            s34: "کاربر قدیمی",
            s35: "کاربر جدید",
            s36: "اعمال تغییرات",
            s37: "ویرایش کاربر",
            s38: "ایجاد کاربر",
            s39: "./events",
            s40: "وضعیت کاربر",
            s41: "فعال",
            s42: "غیر فعال",
            s43: "قفل شده",
            s44: "اطلاعات کاربر",
            s45: "رمز عبور",
            s46: "رمز عبور جدید",
            s47: "تکرار رمز عبور جدید",
            s48: "رمز عبور شما باید شامل موارد زیر باشد:",
            s49: "رمز عبور های وارد شده یکسان نمی باشند",
            s50: "کاربری با این شناسه وجود دارد، شناسه دیگری انتخاب کنید.",
            s51: "زمان انقضا کاربر",
            s52: "ایمیل بازنشانی رمز عبور با موفقیت ارسال شد.",
            U0: "رمز عبور",
            U1: "کاربران",
            U2: "شناسه",
            U3: "نام (به انگلیسی)",
            U4: "نام خانوادگی (به انگلیسی)",
            U5: "نام",
            U6: "شماره تلفن",
            U7: "ایمیل",
            U8: "کد ملی",
            U9: "توضیحات",
            U10: "به روزرسانی",
            U11: "حذف",
            U12: "کاربر جدید",
            U13: "ویرایش",
            U14: "گروه های عضو",
            U15: "تکرار رمز عبور",
            U16: "کاربر مورد نظر در گروههای زیر عضویت دارد. کاربر مورد نظر از چه گروههایی حذف شود؟",
            U17: "حذف همه",
            U18: "وارد کردن کاربران با فایل",
            U19: "ذخیره سازی داده ها در فایل",
            U20: "وارد کردن کاربران با فایل",
            U21: "بارگزاری",
            U22: "بازنشانی رمز عبور",
            U23: "فایل الگو"
        },
        created: function () {
            this.getUserInfo();
            this.isAdmin();
            this.getUserPic();
            this.getUsers();
            this.getGroups();
            if(typeof this.$route.query.en !== 'undefined'){
                this.changeLang();
            }
        },
        methods: {
            isActive (menuItem) {
                return this.activeItem === menuItem
            },
            setActive (menuItem) {
                this.activeItem = menuItem
            },
            selectedFile() {
                this.ImportedFile = this.$refs.file.files[0];
                const file = this.$refs.file.files[0];
                var re = /(\.xls|\.xlsx|\.csv|\.ldif)$/i;

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
                            var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                            var vm = this;
                            var bodyFormData = new FormData();
                            bodyFormData.append('file', this.ImportedFile);
                            axios({
                                method: 'post',
                                url: url + "/api/users/import",  //
                                headers: {'Content-Type': 'multipart/form-data'},
                                data: bodyFormData,
                            })
                            .then((res) => {
                                location.reload();
                            })
                            .catch((error) => {
                                if(error.response){
                                    vm.usernameList = error.response.data;
                                    for(var i = 0; i < error.response.data.length; ++i){
                                        vm.usersCorrectList.push(error.response.data[i].old);
                                        if(error.response.data[i].old.firstName != error.response.data[i].new.firstName){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"firstName"},
                                            {"oldValue":error.response.data[i].old.firstName},
                                            {"newValue":error.response.data[i].new.firstName}]);
                                        }
                                        if(error.response.data[i].old.lastName != error.response.data[i].new.lastName){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"lastName"},
                                            {"oldValue":error.response.data[i].old.lastName},
                                            {"newValue":error.response.data[i].new.lastName}]);
                                        }
                                        if(error.response.data[i].old.displayName != error.response.data[i].new.displayName){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"displayName"},
                                            {"oldValue":error.response.data[i].old.displayName},
                                            {"newValue":error.response.data[i].new.displayName}]);
                                        }
                                        if(error.response.data[i].old.mobile != error.response.data[i].new.mobile){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"mobile"},
                                            {"oldValue":error.response.data[i].old.mobile},
                                            {"newValue":error.response.data[i].new.mobile}]);
                                        }
                                        if(error.response.data[i].old.mail != error.response.data[i].new.mail){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"mail"},
                                            {"oldValue":error.response.data[i].old.mail},
                                            {"newValue":error.response.data[i].new.mail}]);
                                        }
                                        if(error.response.data[i].old.memberOf.length != error.response.data[i].new.memberOf.length){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"memberOf"},
                                            {"oldValue":error.response.data[i].old.memberOf},
                                            {"newValue":error.response.data[i].new.memberOf}]);
                                        }else{
                                            var flag = false;
                                            for(var j = 0; j < error.response.data[i].old.memberOf.length; ++j){
                                                if(error.response.data[i].old.memberOf[j] != error.response.data[i].new.memberOf[j]){
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                            if(flag){
                                                vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                                {"name":"memberOf"},
                                                {"oldValue":error.response.data[i].old.memberOf},
                                                {"newValue":error.response.data[i].new.memberOf}]);
                                            }
                                        }
                                        if(error.response.data[i].old.description != error.response.data[i].new.description){
                                            vm.usersConflictList.push([{"userId":error.response.data[i].old.userId},
                                            {"name":"description"},
                                            {"oldValue":error.response.data[i].old.description},
                                            {"newValue":error.response.data[i].new.description}]);
                                        }
                                    }
                                }
                                vm.showS = "display:none";
                                vm.addS = "display:none";
                                vm.editS = "display:none";
                                vm.importConflictS = "";
                            });
                        }
                    }
                }
            },
            selectConflict:function(s1, s2) {
                document.getElementById(s1).className = "btn btn-success mb-2";
                document.getElementById(s2).className = "btn btn-notSelected mb-2";
                var res = s1.split("-");
                this.usersCorrectUserIdList.push(res[0]);
                for(var i = 0; i < this.usersCorrectList.length; ++i){
                    if(this.usersCorrectList[i].userId == res[0]){
                        if(res[1] == "firstName"){
                            this.usersCorrectList[i].firstName = res[2];
                        }else if(res[1] == "lastName"){
                            this.usersCorrectList[i].lastName = res[2];
                        }else if(res[1] == "displayName"){
                            this.usersCorrectList[i].displayName = res[2];
                        }else if(res[1] == "mobile"){
                            this.usersCorrectList[i].mobile = res[2];
                        }else if(res[1] == "mail"){
                            this.usersCorrectList[i].mail = res[2];
                        }else if(res[1] == "description"){
                            this.usersCorrectList[i].description = res[2];
                        }else if(res[1] == "memberOf"){
                            this.usersCorrectList[i].memberOf = res[2];
                        }
                    }
                }
            },
            unselectConflict:function(s) {
                document.getElementById(s).className = "btn btn-notSelected mb-2";
            },
            resolveConflicts:function() {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                for(var i = 0; i < this.usersCorrectList.length; ++i){
                    if(this.usersCorrectUserIdList.indexOf(this.usersCorrectList[i].userId) != -1){
                        var groupsConflictList = [];
                        var groupsList = this.usersCorrectList[i].memberOf.split(',');
                        for(var j = 0; j < groupsList.length; ++j){
                            groupsConflictList.push(groupsList[j]);
                        }
                        axios({
                            method: 'put',
                            url: url + '/api/users/u/' + vm.usersCorrectList[i].userId,  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                userId: vm.usersCorrectList[i].userId,
                                firstName: vm.usersCorrectList[i].firstName,
                                lastName: vm.usersCorrectList[i].lastName,
                                displayName: vm.usersCorrectList[i].displayName,
                                mobile: vm.usersCorrectList[i].mobile,
                                memberOf: groupsConflictList,
                                mail: vm.usersCorrectList[i].mail,
                                description: vm.usersCorrectList[i].description,
                            }),
                        });
                    }
                }
                location.reload();
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
            isAdmin: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/user/isAdmin") //
                  .then((res) => {
                    if(res.data == "0"){
                      vm.menuSA = true;
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
            addGroupCreate: function (n) {
                n = n.split("'").join("");
                if(this.groupListCreate.includes(n)){
                  if(this.groupListCreate.includes(n + ',')){
                    this.groupListCreate = this.groupListCreate.replace(n + ',', "");
                  }else if(this.groupListCreate === n){
                    this.groupListCreate = this.groupListCreate.replace(n, "");
                  }else{
                    this.groupListCreate = this.groupListCreate.replace(',' + n, "");
                  }
                }else{
                  if(this.groupListCreate === ""){
                    this.groupListCreate += n;
                  }else{
                    this.groupListCreate += ',' + n;
                  }
                }
            },
            addGroupUpdate: function (n) {
                n = n.split("'").join("");
                if(this.groupListUpdate.includes(n)){
                  if(this.groupListUpdate.includes(n + ',')){
                    this.groupListUpdate = this.groupListUpdate.replace(n + ',', "");
                  }else if(this.groupListUpdate === n){
                    this.groupListUpdate = this.groupListUpdate.replace(n, "");
                  }else{
                    this.groupListUpdate = this.groupListUpdate.replace(',' + n, "");
                  }
                }else{
                  if(this.groupListUpdate === ""){
                    this.groupListUpdate += n;
                  }else{
                    this.groupListUpdate += ',' + n;
                  }
                }
            },
            getUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/users") //
                    .then((res) => {
                        vm.users = res.data;
                        vm.total = Math.ceil(vm.users.length / vm.recordsShownOnPage);
                    });
            },
            showUsers: function () {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                this.importConflictS = "display:none"
                location.reload();
            },
            showImportConflicts: function () {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = "display:none"
                this.importConflictS = ""
            },
            editUserS: function (id) {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = ""
                this.importConflictS = "display:none"
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.getGroups();
                axios.get(url + `/api/users/u/${id}`) //
                    .then((res) => {
                        vm.editInfo = res.data;
                        if(typeof res.data.status !== 'undefined'){
                            vm.userStatus = res.data.status;
                            if(res.data.status == "active"){
                                document.getElementById("option1").selected = true;
                                document.getElementById("option2").selected = false;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "disabled"){
                                document.getElementById("option1").selected = false;
                                document.getElementById("option2").selected = true;
                                document.getElementById("option3").selected = false;
                            }else if(res.data.status == "locked"){
                                document.getElementById("option1").selected = false;
                                document.getElementById("option2").selected = false;
                                document.getElementById("option3").selected = true;
                            }
                        }

                        if(typeof res.data.endTime !== 'undefined'){
                            let seTime = res.data.endTime;
                            persianDate.toCalendar('gregorian');
                            let dayWrapper = new persianDate([seTime.substring(0,4), seTime.substring(5,7), seTime.substring(8,10),
                              seTime.substring(11,13), seTime.substring(14,16), seTime.substring(17,19), seTime.substring(20,23)]);
                            document.getElementById("endTime").value = dayWrapper.toCalendar('persian').format("dddd DD MMMM YYYY  HH:mm  a");
                        }

                        if(typeof res.data.memberOf !== 'undefined'){
                            for(let i = 0; i < res.data.memberOf.length; ++i){
                                document.getElementById("groupNameId" + res.data.memberOf[i]).checked = true;
                                if(vm.groupListUpdate === ""){
                                    vm.groupListUpdate += res.data.memberOf[i];
                                }else{
                                    vm.groupListUpdate += ',' + res.data.memberOf[i];
                                }
                            }
                        }
                    });
            },
            editUser: function (id) {

                if(id == "" ||
                document.getElementById('editInfo.displayNameUpdate').value == "" ||
                document.getElementById('editInfo.mobileUpdate').value == "" ||
                document.getElementById('editInfo.mailUpdate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    this.showS = ""
                    this.addS = "display:none"
                    this.editS = "display:none"
                    this.importConflictS = "display:none"
                    let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                    var check = confirm(this.s26);

                    var checkedGroups = [];
                    if(document.getElementById('groupsUpdate').value != ""){
                        checkedGroups = document.getElementById('groupsUpdate').value.split(',');
                    }

                    let endTimeFinal = null;
                    if(document.getElementById('endTime').value != ""){
                        let dateEndTemp = document.getElementById('endTime').value.split("  ");
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

                        let timeEnd = dateEndTemp[1].split(':');

                        timeEnd = this.FaNumToEnNum(timeEnd[0]) + ':' + this.FaNumToEnNum(timeEnd[1]);
                        
                        let dateE = dateEndFinal.split('-');

                        if(parseInt(dateE[1]) < 7){
                        timeEnd = timeEnd + ":00.000+4:30";
                        }else{
                        timeEnd = timeEnd + ":00.000+3:30";
                        }

                        let TempE = timeEnd.split(':');
                        if(TempE[0].length == 1){
                        TempE[0] = '0' + TempE[0];
                        timeEnd = "";
                        for(i = 0; i < TempE.length; ++i){
                            timeEnd = timeEnd + TempE[i] + ':';
                        }
                        timeEnd = timeEnd.substring(0,timeEnd.length-1);
                        }

                        TempE = dateEndFinal.split('-');
                        if(TempE[1].length == 1){
                        TempE[1] = '0' + TempE[1];
                        }
                        if(TempE[2].length == 1){
                        TempE[2] = '0' + TempE[2];
                        }
                        
                        dateEndFinal = TempE[0] + '-' + TempE[1] + '-' + TempE[2];

                        endTimeFinal = dateEndFinal + "T" + timeEnd;
                    }

                    if (check == true) {
                        let statusValue;
                        if(document.getElementById('status').value != "locked"){
                            if(document.getElementById('status').value == "active"){
                                if(this.userStatus == "active"){
                                    statusValue = "";
                                }else if(this.userStatus == "disabled"){
                                    statusValue = "enable";
                                }else if(this.userStatus == "locked"){
                                    statusValue = "unlock";
                                }
                            }else if(document.getElementById('status').value == "disabled"){
                                if(this.userStatus == "active"){
                                    statusValue = "disable";
                                }else if(this.userStatus == "disabled"){
                                    statusValue = "";
                                }else if(this.userStatus == "locked"){
                                    statusValue = "disable";
                                }
                            }
                        }else{
                            statusValue = "";
                        }

                        axios({
                            method: 'put',
                            url: url + '/api/users/u/' + id,  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                userId: id,
                                firstName: document.getElementById('editInfo.firstNameUpdate').value,
                                lastName: document.getElementById('editInfo.lastNameUpdate').value,
                                displayName: document.getElementById('editInfo.displayNameUpdate').value,
                                mobile: document.getElementById('editInfo.mobileUpdate').value,
                                memberOf: checkedGroups,
                                mail: document.getElementById('editInfo.mailUpdate').value,
                                description: document.getElementById('editInfo.descriptionUpdate').value,
                                cStatus: statusValue,
                                endTime: endTimeFinal
                            })
                        })
                        .then((res) => {
                            location.reload();
                        });
                        
                    }
                }
            },
            editPass: function (id) {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
                this.importConflictS = "display:none"
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var check = confirm(this.s26);

                if (check == true) {
                    axios({
                        method: 'put',
                        url: url + '/api/users/u/' + id,  //
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify({
                            userPassword: document.getElementById('newPassword').value
                        }),
                    })
                    .then((res) => {
                        location.reload();
                    });
                }

            },
            exportUsers: function(){
                url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;

                axios.get(url + "/api/users/full") //
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
                this.importConflictS = "display:none"
                this.getGroups();
            },
            sendResetEmail(userId) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/users/u/" + userId) //
                    .then((res) => {
                        axios.get(url + "/api/users/sendMail/" + res.data.mail + "/" + userId) //
                            .then((res) => {
                                vm.resetPassEmailSent = true;
                                setTimeout(function(){ vm.resetPassEmailSent = false; }, 3000);
                            });
                    })
            },
            removeError() {
                this.userFound = false;
            },
            addUser: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;

                if(document.getElementById('editInfo.userIdCreate').value == "" ||
                document.getElementById('editInfo.displayNameCreate').value == "" ||
                document.getElementById('editInfo.mailCreate').value == "" ||
                document.getElementById('editInfo.mobileCreate').value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{

                    var check = confirm(this.s27);

                    var checkedGroups = [];
                    if(document.getElementById('groupsCreate').value != ""){
                        checkedGroups = document.getElementById('groupsCreate').value.split(',');
                    }

                    let endTimeFinal = null;
                    if(document.getElementById('endTimeCreate').value != ""){
                        let dateEndTemp = document.getElementById('endTimeCreate').value.split("  ");
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

                        let timeEnd = dateEndTemp[1].split(':');

                        timeEnd = this.FaNumToEnNum(timeEnd[0]) + ':' + this.FaNumToEnNum(timeEnd[1]);
                        
                        let dateE = dateEndFinal.split('-');

                        if(parseInt(dateE[1]) < 7){
                        timeEnd = timeEnd + ":00.000+4:30";
                        }else{
                        timeEnd = timeEnd + ":00.000+3:30";
                        }

                        let TempE = timeEnd.split(':');
                        if(TempE[0].length == 1){
                        TempE[0] = '0' + TempE[0];
                        timeEnd = "";
                        for(i = 0; i < TempE.length; ++i){
                            timeEnd = timeEnd + TempE[i] + ':';
                        }
                        timeEnd = timeEnd.substring(0,timeEnd.length-1);
                        }

                        TempE = dateEndFinal.split('-');
                        if(TempE[1].length == 1){
                        TempE[1] = '0' + TempE[1];
                        }
                        if(TempE[2].length == 1){
                        TempE[2] = '0' + TempE[2];
                        }
                        
                        dateEndFinal = TempE[0] + '-' + TempE[1] + '-' + TempE[2];

                        endTimeFinal = dateEndFinal + "T" + timeEnd;
                    }

                    if(check==true) {
                        axios({
                            method: 'post',
                            url: url + "/api/users",  //
                            headers: {'Content-Type': 'application/json'},
                            data: JSON.stringify({
                                    userId: document.getElementById('editInfo.userIdCreate').value,
                                    firstName: document.getElementById('editInfo.firstNameCreate').value,
                                    lastName: document.getElementById('editInfo.lastNameCreate').value,
                                    displayName: document.getElementById('editInfo.displayNameCreate').value,
                                    mobile: document.getElementById('editInfo.mobileCreate').value,
                                    memberOf: checkedGroups,
                                    mail: document.getElementById('editInfo.mailCreate').value,
                                    userPassword: document.getElementById('newPasswordCreate').value,
                                    description: document.getElementById('editInfo.descriptionCreate').value,
                                    cStatus: document.getElementById('statusCreate').value,
                                    endTime: endTimeFinal
                                }
                            ),
                        })
                        .then(() => {
                            location.reload();
                        })
                        .catch((error) => {
                            if (error.response) {
                                if(error.response.status === 302){
                                    vm.userFound = true;
                                }
                            }
                        });
                    }
                }
            },
            deleteUser: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s28);
                if (check == true) {
                    axios.delete(url + `/api/users/u/${id}`) //
                        .then(() => {
                            vm.getUsers();
                        });
                }
            },
            deleteAllUsers: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                var check = confirm(this.s29);
                if (check == true) {
                    axios.delete(url + `/api/users`) //
                        .then(() => {
                            vm.getUsers();
                        });
                }

            },
            passwordCheck () {
                this.has_number    = /[0-9]+/.test(this.password);
                this.has_lowerUPPERcase = /[a-zA-Z]/.test(this.password);
                this.has_specialchar = /[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.password) || this.persianTextCheck(this.password);
                this.has_char   = /.{8,}/.test(this.password);
            },
            persianTextCheck (s) {
                for (let i = 0; i < s.length; ++i) {
                    if(persianRex.text.test(s.charAt(i))){
                        return true;
                    }
                }
                return false;
            },
            passwordCheckCreate () {
                this.has_numberCreate    = /[0-9]+/.test(this.passwordCreate);
                this.has_lowerUPPERcaseCreate = /[a-zA-Z]/.test(this.passwordCreate);
                this.has_specialcharCreate = /[!@#\$%\^\&*\)\(+=\[\]._-]+/.test(this.passwordCreate) || this.persianTextCheck(this.passwordCreate);
                this.has_charCreate  = /.{8,}/.test(this.passwordCreate);
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
            userIdValidate ($event) {
                let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
                if (keyCode < 48 || keyCode > 122) {
                   $event.preventDefault();
                }else if (keyCode == 58 || keyCode == 62) {
                  $event.preventDefault();
                }
            },
            changeLang: function () {
                if(this.lang == "EN"){
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
                    this.margin3 = "mr-3";
                    this.margin5 = "mr-5";
                    this.padding0Left = "padding-right: 0rem;";
                    this.padding0Right = "padding-left: 0rem;";
                    this.eye = "left: 1%;";
                    this.font = "font-size: 0.9em; text-align: left;"
                    this.s0 = "Parsso";
                    this.s1 = this.nameEN;
                    this.s2 = "Exit";
                    this.s3 = "Reset Password";
                    this.s4 = "Services";
                    this.s5 = "Groups";
                    this.s6 = "Events";
                    this.s7 = "Profile";
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
                    this.s31 = "Configs";
                    this.s32 = "./configs?en";
                    this.s33 = "Resolve Conflicts While Importing Users";
                    this.s34 = "Old User";
                    this.s35 = "New User";
                    this.s36 = "Submit";
                    this.s37 = "Edit User";
                    this.s38 = "Add User";
                    this.s39 = "./events?en";
                    this.s40 = "Status";
                    this.s41 = "Active";
                    this.s42 = "Disabled";
                    this.s43 = "Locked";
                    this.s44 = "Information";
                    this.s45 = "Password";
                    this.s46 = "New Password";
                    this.s47 = "Repeat New Password";
                    this.s48 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s49 = "Passwords Don't Match";
                    this.s50 = "This UID Already Exists In Our Database, Please Choose Another.";
                    this.s51 = "User Expiration Date";
                    this.s52 = "Password Reset Email Sent Successfully.";
                    this.U0 = "Password";
                    this.U1 = "Users";
                    this.U2 = "ID";
                    this.U3 = "First Name (In English)";
                    this.U4 = "Last Name (In English)";
                    this.U5 = "FullName (In Persian)";
                    this.U6 = "Phone";
                    this.U7 = "Email";
                    this.U8 = "NID";
                    this.U9 = "Description";
                    this.U10 = "Update";
                    this.U11 = "Delete"
                    this.U12 = "New User";
                    this.U13 = "Edit";
                    this.U14 = "Groups";
                    this.U17 = "Remove All";
                    this.U18 = "Export Users to a file";
                    this.U19 = "Save in file";
                    this.U20 = "Import users using file";
                    this.U21 = "Upload";
                    this.U22 = "Password Reset";
                    this.U23 = "Template File";
                    this.rules[0].message = "- One Lowercase or Uppercase English Letter Required.";
                    this.rules[1].message = "- One special Character or Persian Letter Required.";
                    this.rules[2].message = "- 8 Characters Minimum.";
                    this.rules[3].message = "- One Number Required.";
                } else{
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
                    this.margin3 = "ml-3";
                    this.margin5 = "ml-5";
                    this.padding0Left = "padding-left: 0rem;";
                    this.padding0Right = "padding-right: 0rem;";
                    this.eye = "right: 1%;";
                    this.font = "font-size: 0.74em; text-align: right;"
                    this.s0 = "پارسو";
                    this.s1 = this.name;
                    this.s2 = "خروج";
                    this.s3 = "بازنشانی رمز عبور";
                    this.s4 = "سرویس ها";
                    this.s5 = "گروه ها";
                    this.s6 = "رویداد ها";
                    this.s7 = "پروفایل";
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
                    this.s31 = "پیکربندی";
                    this.s32 = "./configs";
                    this.s33 = "رفع تناقض وارد کردن کاربران جدید";
                    this.s34 = "کاربر قدیمی";
                    this.s35 = "کاربر جدید";
                    this.s36 = "اعمال تغییرات";
                    this.s37 = "ویرایش کاربر";
                    this.s38 = "ایجاد کاربر";
                    this.s39 = "./events";
                    this.s40 = "وضعیت کاربر";
                    this.s41 = "فعال";
                    this.s42 = "غیر فعال";
                    this.s43 = "قفل شده";
                    this.s44 = "اطلاعات کاربر";
                    this.s45 = "رمز عبور";
                    this.s46 = "رمز عبور جدید";
                    this.s47 = "تکرار رمز عبور جدید";
                    this.s48 = "رمز عبور شما باید شامل موارد زیر باشد:";
                    this.s49 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s50 = "کاربری با این شناسه وجود دارد، شناسه دیگری انتخاب کنید.";
                    this.s51 = "زمان انقضا کاربر";
                    this.s52 = "ایمیل بازنشانی رمز عبور با موفقیت ارسال شد.";
                    this.U0 = "رمز";
                    this.U1 = "کاربران";
                    this.U2 = "شناسه";
                    this.U3 = "نام (به انگلیسی)";
                    this.U4 = "نام خانوادگی (به انگلیسی)";
                    this.U5 = "نام کامل (به فارسی)";
                    this.U6 = "شماره تلفن";
                    this.U7 = "ایمیل";
                    this.U8 = "کد ملی";
                    this.U9 = "توضیحات";
                    this.U10 = "به روز رسانی";
                    this.U11 = "حذف"
                    this.U12 = "کاربر جدید";
                    this.U13 = "ویرایش";
                    this.U14 = "گروه های عضو";
                    this.U17 = "حذف همه";
                    this.U18 = "وارد کردن کاربران با فایل";
                    this.U19 = "ذخیره سازی داده ها در فایل";
                    this.U20 = "وارد کردن کاربران با فایل";
                    this.U21 = "بارگزاری";
                    this.U22 = "بازنشانی رمز عبور";
                    this.U23 = "فایل الگو";
                    this.rules[0].message = "حداقل شامل یک حرف کوچک یا بزرگ انگلیسی باشد. ";
                    this.rules[1].message = "حداقل شامل یک کاراکتر خاص یا حرف فارسی باشد. ";
                    this.rules[2].message = "حداقل ۸ کاراکتر باشد. ";
                    this.rules[3].message = "حداقل شامل یک عدد باشد. ";
                }
            }
        },
        computed:{
            userCreateExists () {
                return this.userFound;
            },
            sortedUsers:function() {
                this.usersPage = [];
                for(let i = 0; i < this.recordsShownOnPage; ++i){
                    if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.users.length - 1){
                        this.usersPage[i] = this.users[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
                    }
                }
                return this.usersPage;
            },
            notSamePasswords () {
                if (this.passwordsFilled) {
                    return (this.password !== this.checkPassword)
                } else {
                    return false
                }
            },
            passwordsFilled () {
                return (this.password !== '' && this.checkPassword !== '')
            },
            isActiveUserPassUpdate () {
                if(this.password !== '' && this.checkPassword !== ''){
                    let errors = []
                    for (let condition of this.rules) {
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                    if(errors.length === 0){
                        if(this.password === this.checkPassword){
                            return false
                        }else{
                            return true
                        }
                    }else{
                        return true
                    }
                }else{
                    return true
                }
            },
            passwordValidation () {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.password) && !this.persianTextCheck(this.password)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if (errors.length === 0) {
                    return { valid:true, errors }
                } else {
                    return { valid:false, errors }
                }
            },
            strengthLevel() {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.password) && !this.persianTextCheck(this.password)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.password)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if(errors.length === 0) return 4;
                if(errors.length === 1) return 3;
                if(errors.length === 2) return 2;
                if(errors.length === 3) return 1;
                if(errors.length === 4) return 0;
            },

            notSamePasswordsCreate () {
                if (this.passwordsFilledCreate) {
                    return (this.passwordCreate !== this.checkPasswordCreate)
                } else {
                    return false
                }
            },
            passwordsFilledCreate () {
                return (this.passwordCreate !== '' && this.checkPasswordCreate !== '')
            },
            isActiveUserPassCreate () {
                if(this.passwordCreate !== '' && this.checkPasswordCreate !== ''){
                    let errors = []
                    for (let condition of this.rules) {
                        if (!condition.regex.test(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }
                    if(errors.length === 0){
                        if(this.passwordCreate === this.checkPasswordCreate){
                            return false
                        }else{
                            return true
                        }
                    }else{
                        return true
                    }
                }else{
                    return true
                }
            },
            passwordValidationCreate () {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.passwordCreate) && !this.persianTextCheck(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if (errors.length === 0) {
                    return { valid:true, errors }
                } else {
                    return { valid:false, errors }
                }
            },
            strengthLevelCreate() {
                let errors = []
                for (let condition of this.rules) {
                    if(condition.fa){
                        if (!condition.regex.test(this.passwordCreate) && !this.persianTextCheck(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }else{
                        if (!condition.regex.test(this.passwordCreate)) {
                            errors.push(condition.message)
                        }
                    }
                }
                if(errors.length === 0) return 4;
                if(errors.length === 1) return 3;
                if(errors.length === 2) return 2;
                if(errors.length === 3) return 1;
                if(errors.length === 4) return 0;
            }
        }
    });
})
