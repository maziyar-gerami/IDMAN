document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    Vue.component('v-pagination', window['vue-plain-pagination'])
    Vue.use(VueQuillEditor)
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
            email: "",
            username: "",
            name: "",
            nameEN: "",
            pubmsg: [],
            pubmsgs: [],
            pubmsgsPage: [],
            pubmsgCreateBody: "",
            pubmsgEditBody: "",
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
            editorOption: {
                theme: "snow",
                placeholder: ""
            },
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
            userPicture: "images/PlaceholderUser.png",
            allIsSelected: false,
            loader: false,
            isListEmpty: false,
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
            s22: "./profile",
            s23: "آیا از اعمال این تغییرات اطمینان دارید؟",
            s24: "آیا از حذف این اعلان عمومی اطمینان دارید؟",
            s25: "آیا از حذف تمامی اعلان های عمومی اطمینان دارید؟",
            s26: "./privacy",
            s27: "پیکربندی",
            s28: "./configs",
            s29: "افزودن اعلان عمومی",
            s30: "ویرایش اعلان عمومی",
            s31: "./events",
            s32: "حذف اعلان عمومی",
            s33: "اعمال",
            s34: "آیا از حذف اعلان های عمومی انتخاب شده اطمینان دارید؟",
            s35: "هیچ اعلان عمومی انتخاب نشده است.",
            s36: "تعداد رکورد ها: ",
            s37: "ممیزی ها",
            s38: "/audits",
            s45: "هیچ اعلان عمومی یافت نشد",
            rolesText: "نقش ها",
            rolesURLText: "./roles",
            reportsText: "گزارش ها",
            reportsURLText: "./reports",
            publicmessagesText: "اعلان های عمومی",
            publicmessagesURLText: "./publicmessages",
            ticketingText: "تیکتینگ",
            ticketingURLText: "./ticketing",
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
            U2: "عنوان",
            U3: "قابل رویت",
            U9: "متن",
            U11: "حذف",
            U12: "جدید",
            U13: "ویرایش",
            U17: "حذف",
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
            this.getPubMsgs();
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
                document.getElementById("overlay").style.display = "block";
            },
            closeOverlay: function () {
                document.getElementById("overlay").style.display = "none";
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
                    for(let i = 0; i < this.pubmsgs.length; ++i){
                        if(document.getElementById("checkbox-" + this.pubmsgs[i].messageId).checked == true){
                            document.getElementById("checkbox-" + this.pubmsgs[i].messageId).click();
                        }
                        document.getElementById("row-" + this.pubmsgs[i].messageId).style.background = "";
                    }
                }else{
                    this.allIsSelected = true;
                    for(let i = 0; i < this.pubmsgs.length; ++i){
                        if(document.getElementById("checkbox-" + this.pubmsgs[i].messageId).checked == false){
                            document.getElementById("checkbox-" + this.pubmsgs[i].messageId).click();
                        }
                        document.getElementById("row-" + this.pubmsgs[i].messageId).style.background = "#c2dbff";
                    }
                }
            },
            changeRecords: function(event) {
                this.recordsShownOnPage = event.target.value;
                this.getPubMsgs();
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
                    if(res.data.skyRoom.enable){
                        vm.showMeeting = true;
                        vm.meetingAdminLink = res.data.skyRoom.presenter;
                        vm.meetingGuestLink = res.data.skyRoom.students;
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
            getPubMsgs: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                this.isListEmpty = false;
                axios.get(url + "/api/users/publicMessages") //
                    .then((res) => {
                        if(res.data.length == 0){
                            vm.isListEmpty = true;
                        }
                        vm.pubmsgs = res.data.slice().reverse();
                        for(let i = 0; i < vm.pubmsgs.length; ++i){
                            vm.pubmsgs[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                        }
                        vm.total = Math.ceil(vm.pubmsgs.length / vm.recordsShownOnPage);
                    });
            },
            showPubMsgs: function () {
                this.showS = ""
                this.addS = "display:none"
                this.editS = "display:none"
            },
            editPubMsgS: function (id) {
                this.showS = "display:none"
                this.addS = "display:none"
                this.editS = ""
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                axios.get(url + "/api/users/publicMessages?id=" + id) //
                    .then((res) => {
                        vm.pubmsg = res.data[0];
                        if(res.data[0].visible){
                            document.getElementById("pubmsg.edit.visible").checked = true;
                        }else {
                            document.getElementById("pubmsg.edit.visible").checked = false;
                        }
                        vm.pubmsgEditBody = res.data[0].body;
                    });
            },
            editPubMsg: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let isVisible = true;
                if(document.getElementById("pubmsg.edit.title").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    var check = confirm(this.s23);
                    if (check == true) {
                        if(document.getElementById("pubmsg.edit.visible").checked) {
                            isVisible = true;
                        }else {
                            isVisible = false;
                        }
                        axios({
                            method: "put",
                            url: url + "/api/users/publicMessage", //
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                                messageId: vm.pubmsg.messageId,
                                title: document.getElementById("pubmsg.edit.title").value,
                                visible: isVisible,
                                body: vm.pubmsgEditBody,
                            }).replace(/\\\\/g, "\\")
                        })
                        .then((res) => {
                            location.reload();
                        }).catch((error) => {
                            alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                        });
                    }
                }
            },
            addPubMsgS: function () {
                this.showS = "display:none"
                this.addS = ""
                this.editS = "display:none"
            },
            addPubMsg: function () {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let isVisible = true;
                if(document.getElementById("pubmsg.create.title").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    if(document.getElementById("pubmsg.create.visible").checked) {
                        isVisible = true;
                    }else {
                        isVisible = false;
                    }
                    axios({
                        method: "post",
                        url: url + "/api/users/publicMessage", //
                        headers: {"Content-Type": "application/json"},
                        data: JSON.stringify({
                            title: document.getElementById("pubmsg.create.title").value,
                            visible: isVisible,
                            body: vm.pubmsgCreateBody,
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        location.reload();
                    }).catch((error) => {
                        alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                    });
                }
            },
            deletePubMsg: function (id) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedPubMsgs = [];
                selectedPubMsgs.push(id.toString());
                var check = confirm(this.s24);
                if (check == true) {
                    axios({
                        method: "delete",
                        url: url + "/api/users/publicMessages", //
                        headers: {"Content-Type": "application/json"},
                        data: JSON.stringify({
                            names: selectedPubMsgs
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        vm.getPubMsgs();
                    }).catch((error) => {
                        alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                    });
                }
            },
            changeSelected: function (action) {
                var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                var vm = this;
                let selectedPubMsgs = [];
                if(action == "delete"){
                    selectedPubMsgs = [];
                    for(let i = 0; i < vm.pubmsgs.length; ++i){
                        if(document.getElementById("checkbox-" + vm.pubmsgs[i].messageId).checked){
                            selectedPubMsgs.push(vm.pubmsgs[i].messageId.toString());
                        }
                    }
                    if(selectedPubMsgs.length != 0){
                        var check = confirm(this.s34);
                        if (check == true) {
                            axios({
                                method: "delete",
                                url: url + "/api/users/publicMessages", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedPubMsgs
                                }).replace(/\\\\/g, "\\")
                            })
                            .then((res) => {
                                vm.getPubMsgs();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }else{
                        alert(this.s35);
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
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.margin = "margin-left: 30px;";
                    this.lang = "فارسی";
                    this.isRtl = false;
                    this.margin1 = "mr-1";
                    this.dateNavText = this.dateNavEn;
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
                    this.s17 = "Go Back";
                    this.s18 = "Submit";
                    this.s19 = "Email";
                    this.s20 = "Dashboard";
                    this.s23 = "Are You Sure You Want To Edit?";
                    this.s24 = "Are You Sure You Want To Delete?";
                    this.s25 = "Are You Sure You Want To Delete All Public Messages?";
                    this.s27 = "Configs";
                    this.s29 = "Add Public Message";
                    this.s30 = "Edit Public Message";
                    this.s32 = "Delete Public Message";
                    this.s33 = "Action";
                    this.s34 = "Are You Sure You Want To Delete Selected Public Messages?";
                    this.s35 = "No Public Message is Selected.";
                    this.s36 = "Records a Page: ";
                    this.s37 = "Audits";
                    this.s45 = "No Public Message Found";
                    this.rolesText = "Roles";
                    this.reportsText = "Reports";
                    this.publicmessagesText = "Public Messages";
                    this.ticketingText = "Ticketing";
                    this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingText = "Meeting";
                    this.enterMeetingText = "Enter Meeting";
                    this.inviteToMeetingText = "Invite To Meeting";
                    this.copyText = "Copy";
                    this.returnText = "Return";
                    this.U2= "Title";
                    this.U3= "Visible";
                    this.U9 = "Description";
                    this.U11 = "Delete"
                    this.U12 = "New";
                    this.U13 = "Edit";
                    this.U17 = "Delete";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
                    this.dateNavText = this.dateNav;
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
                    this.s17 = "بازگشت";
                    this.s18 = "تایید";
                    this.s19 = "ایمیل";
                    this.s20 = "داشبورد";
                    this.s23 = "آیا از اعمال این تغییرات اطمینان دارید؟";
                    this.s24 = "آیا از حذف این اعلان عمومی اطمینان دارید؟";
                    this.s25 = "آیا از حذف تمامی اعلان های عمومی اطمینان دارید؟";
                    this.s26 = "./privacy";
                    this.s27 = "پیکربندی";
                    this.s29 = "افزودن اعلان عمومی";
                    this.s30 = "ویرایش اعلان عمومی";
                    this.s32 = "حذف اعلان عمومی";
                    this.s33 = "اعمال";
                    this.s34 = "آیا از حذف اعلان های عمومی انتخاب شده اطمینان دارید؟";
                    this.s35 = "هیچ اعلان عمومی انتخاب نشده است.";
                    this.s36 = "تعداد رکورد ها: ";
                    this.s37 = "ممیزی ها";
                    this.s45 = "هیچ اعلان عمومی یافت نشد";
                    this.rolesText = "نقش ها";
                    this.reportsText = "گزارش ها";
                    this.publicmessagesText = "اعلان های عمومی";
                    this.ticketingText = "تیکتینگ";
                    this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingText = "جلسه مجازی";
                    this.enterMeetingText = "ورود به جلسه";
                    this.inviteToMeetingText = "دعوت به جلسه";
                    this.copyText = "کپی";
                    this.returnText = "بازگشت";
                    this.U2 = "عنوان";
                    this.U3 = "قابل رویت";
                    this.U9 = "متن";
                    this.U11 = "حذف"
                    this.U12 = "جدید";
                    this.U13 = "ویرایش";
                    this.U17 = "حذف"
                }
            }
        },
        computed:{
            sortedPubMsgs:function() {
                this.pubmsgsPage = [];
                for(let i = 0; i < this.recordsShownOnPage; ++i){
                    if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.pubmsgs.length - 1){
                        this.pubmsgsPage[i] = this.pubmsgs[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
                    }
                }
                return this.pubmsgsPage;
            },
            editor() {
                return this.$refs.quillEditor.quill
            },
        }
    });
})
