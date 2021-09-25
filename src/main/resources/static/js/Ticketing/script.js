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
            userInfo: {},
            email: "",
            username: "",
            name: "",
            nameEN: "",
            ticket: [],
            ticketDetail: [],
            tickets: [],
            inboxTickets: [],
            archivesTickets: [],
            ticketsPage: [],
            inboxTicketsPage: [],
            archivesTicketsPage: [],
            editInfo: {},
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            lang: "EN",
            isRtl: true,
            listS: "",
            replyS: "display:none",
            createS: "display:none",
            detailS: "display:none",
            recordsShownOnPage: 20,
            currentPage: 1,
            total: 1,
            paginationCurrentPage: 1,
            recordsShownOnInboxPage: 20,
            currentInboxPage: 1,
            totalInbox: 1,
            paginationCurrentInboxPage: 1,
            recordsShownOnArchivesPage: 20,
            currentArchivesPage: 1,
            totalArchives: 1,
            paginationCurrentArchivesPage: 1,
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
            allInboxIsSelected: false,
            loader: false,
            overlayLoader: false,
            isListEmpty: false,
            isInboxListEmpty: false,
            isArchivesListEmpty: false,
            activeItem: "chatsTab",
            searchStatus: "",
            showMeeting: false,
            meetingInviteLinkStyle: "border-top-left-radius: 0;border-bottom-left-radius: 0;",
            meetingInviteLinkCopyStyle: "border-top-right-radius: 0;border-bottom-right-radius: 0;",
            meetingAdminLink: "",
            meetingGuestLink: "",
            deleteInputIcon: "left: 10%;",
            idSearch: "",
            inboxIdSearch: "",
            archivesIdSearch: "",
            dateSearch: "",
            inboxDateSearch: "",
            archivesDateSearch: "",
            s0: "احراز هویت متمرکز شرکت فلات قاره",
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
            s24: "آیا از حذف این تیکت اطمینان دارید؟",
            s25: "آیا از حذف تمامی تیکت ها اطمینان دارید؟",
            s26: "./privacy",
            s27: "پیکربندی",
            s28: "./configs",
            s29: "افزودن تیکت",
            s30: "ویرایش تیکت",
            s31: "./events",
            s32: "حذف تیکت",
            s33: "اعمال",
            s34: "آیا از حذف تیکت های انتخاب شده اطمینان دارید؟",
            s35: "هیچ تیکتی انتخاب نشده است.",
            s36: "تعداد رکورد ها: ",
            s37: "ممیزی ها",
            s38: "/audits",
            s45: "هیچ تیکتی یافت نشد",
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
            meetingText: "جلسه مجازی",
            enterMeetingText: "ورود به جلسه",
            inviteToMeetingText: "دعوت به جلسه",
            copyText: "کپی",
            returnText: "بازگشت",
            replyText: "پاسخ",
            subjectText: "موضوع",
            fromText: "ارسال کننده",
            sendNewMessageText: "ارسال پیام جدید",
            messageText: "پیام",
            newText: "جدید",
            deleteText: "حذف",
            sendText: "ارسال",
            sendAndCloseText: "ارسال و بستن تیکت",
            openTicketsText: "تیکت های باز",
            myOpenTicketsText: "تیکت های باز من",
            allText: "همه",
            ticketsListText: "لیست تیکت ها",
            closeText: "بستن",
            pendingTicketsText: "تیکت های در حال انتظار",
            closedTicketsText: "تیکت های بسته شده",
            createDateText: "تاریخ ایجاد",
            idText: "شناسه",
            submitFilter: "اعمال فیلتر",
            removeFilter: "حذف فیلتر",
            closeSelectedTicketsText: "آیا از بستن تیکت های انتخاب شده اطمینان دارید؟",
            statusText: "وضعیت",
            openText: "باز",
            pendingText: "در حال انتظار",
            closedText: "بسته شده",
            alertMessage1Text: "تیکت در تاریخ ",
            alertMessage2Text: " توسط کاربر ",
            alertMessageOpen3Text: " باز شد.",
            alertMessageClose3Text: " بسته شد.",
            unreadMessageText: "پیام پاسخ داده نشده",
            ticketArchivesText: "آرشیو تیکت ها",
            detailText: "جزئیات",
        },
        created: function () {
            this.setDateNav();
            this.getUserInfo();
            this.getUserPic();
            this.getTickets();
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
            faMonthtoNumMonth: function (faMonth) {
                let numMonth = "";
                switch(faMonth) {
                    case "فروردین":
                        numMonth = "01";
                        break;
                    case "اردیبهشت":
                        numMonth = "02";
                        break;
                    case "خرداد":
                        numMonth = "03";
                        break;
                    case "تیر":
                        numMonth = "04";
                        break;
                    case "مرداد":
                        numMonth = "05";
                        break;
                    case "شهریور":
                        numMonth = "06";
                        break;
                    case "مهر":
                        numMonth = "07";
                        break;
                    case "آبان":
                        numMonth = "08";
                        break;
                    case "آذر":
                        numMonth = "09";
                        break;
                    case "دی":
                        numMonth = "10";
                        break;
                    case "بهمن":
                        numMonth = "11";
                        break;
                    case "اسفند":
                        numMonth = "12";
                        break;
                    default:
                        console.log("Wrong Input for Month");
                }
                return numMonth;
            },
            faNumToEnNum: function (str) {
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
            isActive (menuItem) {
                return this.activeItem === menuItem
            },
            setActive (menuItem) {
                this.activeItem = menuItem
            },
            allSelected: function () {
                if(this.allIsSelected){
                    this.allIsSelected = false;
                    for(let i = 0; i < this.tickets.length; ++i){
                        if(document.getElementById("checkbox-" + this.tickets[i].id).checked == true){
                            document.getElementById("checkbox-" + this.tickets[i].id).click();
                        }
                        document.getElementById("row-" + this.tickets[i].id).style.background = "";
                    }
                }else{
                    this.allIsSelected = true;
                    for(let i = 0; i < this.tickets.length; ++i){
                        if(document.getElementById("checkbox-" + this.tickets[i].id).checked == false){
                            document.getElementById("checkbox-" + this.tickets[i].id).click();
                        }
                        document.getElementById("row-" + this.tickets[i].id).style.background = "#c2dbff";
                    }
                }
            },
            allInboxSelected: function () {
                if(this.allInboxIsSelected){
                    this.allInboxIsSelected = false;
                    for(let i = 0; i < this.inboxTickets.length; ++i){
                        if(document.getElementById("inbox-checkbox-" + this.inboxTickets[i].id).checked == true){
                            document.getElementById("inbox-checkbox-" + this.inboxTickets[i].id).click();
                        }
                        document.getElementById("inbox-row-" + this.inboxTickets[i].id).style.background = "";
                    }
                }else{
                    this.allInboxIsSelected = true;
                    for(let i = 0; i < this.inboxTickets.length; ++i){
                        if(document.getElementById("inbox-checkbox-" + this.inboxTickets[i].id).checked == false){
                            document.getElementById("inbox-checkbox-" + this.inboxTickets[i].id).click();
                        }
                        document.getElementById("inbox-row-" + this.inboxTickets[i].id).style.background = "#c2dbff";
                    }
                }
            },
            changeRecords: function(event) {
                this.recordsShownOnPage = event.target.value;
                this.getTickets();
            },
            changeInboxRecords: function(event) {
                this.recordsShownOnInboxPage = event.target.value;
                if(userInfo.role == "SUPPORTER" || userInfo.role == "SUPERADMIN"){
                    this.getInboxTickets();
                }else {
                    this.getSentTickets();
                }
            },
            changeArchivesRecords: function(event) {
                this.recordsShownOnArchivesPage = event.target.value;
                if(userInfo.role == "SUPERADMIN"){
                    this.getArchivesTickets();
                }
            },
            getUserInfo: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                axios.get(url + "/api/user") //
                .then((res) => {
                    vm.userInfo = res.data;
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
                    if(res.data.role == "SUPPORTER" || res.data.role == "SUPERADMIN"){
                        vm.getInboxTickets();
                    }else {
                        vm.getSentTickets();
                    }
                    if(res.data.role == "SUPERADMIN"){
                        vm.getArchivesTickets();
                    }
                });
            },
            getUserPic: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
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
            getTickets: function (p) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let searchQuery = "";
                let searchQueryInit = "?";
                if(this.searchStatus != ""){
                    searchQuery = searchQuery + searchQueryInit + "status=" + this.searchStatus;
                    searchQueryInit = "&";
                }
                if(this.idSearch != ""){
                    searchQuery = searchQuery + searchQueryInit + "id=" + this.idSearch;
                    searchQueryInit = "&";
                }
                this.dateSearch = document.getElementById("dateSearch").value;
                if(this.dateSearch != ""){
                    let tempArray = this.dateSearch.split(" ");
                    this.dateSearch = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
                    searchQuery = searchQuery + searchQueryInit + "date=" + this.dateSearch;
                    searchQueryInit = "&";
                }
                if(p != "paginationCurrentPage"){
                    this.currentPage = 1;
                    this.paginationCurrentPage = this.currentPage;
                }
                this.tickets = [];
                this.isListEmpty = false;
                axios.get(url + "/api/supporter/tickets/" + vm.currentPage + "/" + vm.recordsShownOnPage + searchQuery) //
                    .then((res) => {
                        vm.total = Math.ceil(res.data.size / vm.recordsShownOnPage);
                        vm.tickets = res.data.ticketList;
                        for(let i = 0; i < vm.recordsShownOnPage && i < res.data.size; ++i){
                            vm.tickets[i].orderOfRecords =  ((vm.currentPage - 1) * vm.recordsShownOnPage) + (i + 1);
                        }
                        if(res.data.ticketList.length == 0){
                            vm.isListEmpty = true;
                        }
                    });
            },
            getInboxTickets: function (p) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let answeredTickets = [];
                let unansweredTickets = [];
                let searchQuery = "";
                let searchQueryInit = "?";
                if(this.inboxIdSearch != ""){
                    searchQuery = searchQuery + searchQueryInit + "id=" + this.inboxIdSearch;
                    searchQueryInit = "&";
                }
                this.inboxDateSearch = document.getElementById("inboxDateSearch").value;
                if(this.inboxDateSearch != ""){
                    let tempArray = this.inboxDateSearch.split(" ");
                    this.inboxDateSearch = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
                    searchQuery = searchQuery + searchQueryInit + "date=" + this.inboxDateSearch;
                    searchQueryInit = "&";
                }
                if(p != "paginationCurrentPage"){
                    this.currentInboxPage = 1;
                    this.paginationCurrentInboxPage = this.currentInboxPage;
                }
                this.inboxTickets = [];
                this.isInboxListEmpty = false;
                axios.get(url + "/api/supporter/tickets/inbox/" + vm.currentInboxPage + "/" + vm.recordsShownOnInboxPage + searchQuery) //
                    .then((res) => {
                        vm.totalInbox = Math.ceil(res.data.size / vm.recordsShownOnInboxPage);
                        for(let i = 0; i < res.data.ticketList.length; ++i){
                            if(res.data.ticketList[i].from == res.data.ticketList[i].lastFrom){
                                res.data.ticketList[i].isUnanswered = true;
                                unansweredTickets.push(res.data.ticketList[i]);
                            }else {
                                res.data.ticketList[i].isUnanswered = false;
                                answeredTickets.push(res.data.ticketList[i]);
                            }
                        }
                        vm.inboxTickets = unansweredTickets.concat(answeredTickets);
                        for(let i = 0; i < vm.recordsShownOnInboxPage && i < res.data.size; ++i){
                            vm.inboxTickets[i].orderOfRecords =  ((vm.currentInboxPage - 1) * vm.recordsShownOnInboxPage) + (i + 1);
                        }
                        if(res.data.ticketList.length == 0){
                            vm.isInboxListEmpty = true;
                        }
                    });
            },
            getArchivesTickets: function (p) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let searchQuery = "";
                let searchQueryInit = "?";
                if(this.archivesIdSearch != ""){
                    searchQuery = searchQuery + searchQueryInit + "id=" + this.archivesIdSearch;
                    searchQueryInit = "&";
                }
                this.archivesDateSearch = document.getElementById("archivesDateSearch").value;
                if(this.archivesDateSearch != ""){
                    let tempArray = this.archivesDateSearch.split(" ");
                    this.archivesDateSearch = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
                    searchQuery = searchQuery + searchQueryInit + "date=" + this.archivesDateSearch;
                    searchQueryInit = "&";
                }
                if(p != "paginationCurrentPage"){
                    this.currentArchivesPage = 1;
                    this.paginationCurrentArchivesPage = this.currentArchivesPage;
                }
                this.archivesTickets = [];
                this.isArchivesListEmpty = false;
                axios.get(url + "/api/superadmin/tickets/archive/" + vm.currentArchivesPage + "/" + vm.recordsShownOnArchivesPage + searchQuery) //
                    .then((res) => {
                        vm.totalArchives = Math.ceil(res.data.size / vm.recordsShownOnArchivesPage);
                        vm.archivesTickets = res.data.ticketList;
                        for(let i = 0; i < vm.recordsShownOnArchivesPage && i < res.data.size; ++i){
                            vm.archivesTickets[i].orderOfRecords =  ((vm.currentArchivesPage - 1) * vm.recordsShownOnArchivesPage) + (i + 1);
                        }
                        if(res.data.ticketList.length == 0){
                            vm.isArchivesListEmpty = true;
                        }
                    });
            },
            getSentTickets: function (p) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let answeredTickets = [];
                let unansweredTickets = [];
                if(p != "paginationCurrentPage"){
                    this.currentInboxPage = 1;
                    this.paginationCurrentInboxPage = this.currentInboxPage;
                }
                this.inboxTickets = [];
                this.isInboxListEmpty = false;
                axios.get(url + "/api/user/tickets/sent/" + vm.currentInboxPage + "/" + vm.recordsShownOnInboxPage) //
                    .then((res) => {
                        vm.totalInbox = Math.ceil(res.data.size / vm.recordsShownOnInboxPage);
                        for(let i = 0; i < res.data.ticketList.length; ++i){
                            if(res.data.ticketList[i].from != res.data.ticketList[i].lastFrom){
                                res.data.ticketList[i].isUnanswered = true;
                                unansweredTickets.push(res.data.ticketList[i]);
                            }else {
                                res.data.ticketList[i].isUnanswered = false;
                                answeredTickets.push(res.data.ticketList[i]);
                            }
                        }
                        vm.inboxTickets = unansweredTickets.concat(answeredTickets);
                        for(let i = 0; i < vm.recordsShownOnInboxPage && i < res.data.size; ++i){
                            vm.inboxTickets[i].orderOfRecords =  ((vm.currentInboxPage - 1) * vm.recordsShownOnInboxPage) + (i + 1);
                        }
                        if(res.data.ticketList.length == 0){
                            vm.isInboxListEmpty = true;
                        }
                    });
            },
            showList: function () {
                this.listS = "";
                this.replyS = "display:none";
                this.createS = "display:none";
                this.detailS = "display:none";
            },
            showCreate: function () {
                this.listS = "display:none";
                this.replyS = "display:none";
                this.createS = "";
                this.detailS = "display:none";
            },
            replyTicket: function (id) {
                this.listS = "display:none";
                this.replyS = "";
                this.createS = "display:none";
                this.detailS = "display:none";
                this.ticket = [];
                /*let chatbox = document.getElementById("chatbox");
                chatbox.scrollTop = chatbox.scrollHeight - chatbox.clientHeight;*/
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                axios.get(url + "/api/user/ticket/" + id) //
                    .then((res) => {
                        vm.ticket = res.data;
                    });
            },
            detailTicket: function (id) {
                this.listS = "display:none";
                this.replyS = "display:none";
                this.createS = "display:none";
                this.detailS = "";
                this.ticketDetail = [];
                /*let chatbox = document.getElementById("chatbox");
                chatbox.scrollTop = chatbox.scrollHeight - chatbox.clientHeight;*/
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                axios.get(url + "/api/user/ticket/" + id) //
                    .then((res) => {
                        vm.ticketDetail = res.data;
                    });
            },
            sendTicketReply: function (id, sendType) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(document.getElementById("messageBody").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    let check = confirm(this.s23);
                    if (check == true) {
                        if(sendType === "send"){
                            axios({
                                method: "put",
                                url: url + "/api/user/ticket/reply/" + id, //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    message: document.getElementById("messageBody").value
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }else if(sendType === "send&close"){
                            axios({
                                method: "put",
                                url: url + "/api/user/ticket/reply/" + id, //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    message: document.getElementById("messageBody").value,
                                    status: 2
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }
                }
            },
            createTicket: function () {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                if(document.getElementById("messageBodyCreate").value == "" ||
                    document.getElementById("ticketCreateSubject").value == ""){
                    alert("لطفا قسمت های الزامی را پر کنید.");
                }else{
                    let check = confirm(this.s23);
                    if (check == true) {
                        axios({
                            method: "post",
                            url: url + "/api/user/ticket", //
                            headers: {"Content-Type": "application/json"},
                            data: JSON.stringify({
                                subject: document.getElementById("ticketCreateSubject").value,
                                message: document.getElementById("messageBodyCreate").value
                            }).replace(/\\\\/g, "\\")
                        }).then((res) => {
                            location.reload();
                        }).catch((error) => {
                            alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                        });
                    }
                }
            },
            deleteTicket: function (id) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let selectedTickets = [];
                selectedTickets.push(id.toString());
                let check = confirm(this.s24);
                if (check == true) {
                    axios({
                        method: "delete",
                        url: url + "/api/user/ticket", //
                        headers: {"Content-Type": "application/json"},
                        data: JSON.stringify({
                            names: selectedTickets
                        }).replace(/\\\\/g, "\\")
                    })
                    .then((res) => {
                        location.reload();
                    }).catch((error) => {
                        alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                    });
                }
            },
            changeSelected: function (action) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let selectedTickets = [];
                if(action == "delete"){
                    selectedTickets = [];
                    for(let i = 0; i < vm.tickets.length; ++i){
                        if(document.getElementById("checkbox-" + vm.tickets[i].id).checked){
                            selectedTickets.push(vm.tickets[i].id.toString());
                        }
                    }
                    if(selectedTickets.length != 0){
                        let check = confirm(this.s34);
                        if (check == true) {
                            axios({
                                method: "delete",
                                url: url + "/api/user/ticket", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedTickets
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }else{
                        alert(this.s35);
                    }
                }else if(action == "close"){
                    selectedTickets = [];
                    for(let i = 0; i < vm.tickets.length; ++i){
                        if(document.getElementById("checkbox-" + vm.tickets[i].id).checked){
                            selectedTickets.push(vm.tickets[i].id.toString());
                        }
                    }
                    if(selectedTickets.length != 0){
                        let check = confirm(this.closeSelectedTicketsText);
                        if (check == true) {
                            axios({
                                method: "put",
                                url: url + "/api/supporter/ticket/status/2", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedTickets
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }else{
                        alert(this.s35);
                    }
                }
            },
            changeInboxSelected: function (action) {
                let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
                let vm = this;
                let selectedInboxTickets = [];
                if(action == "delete"){
                    selectedInboxTickets = [];
                    for(let i = 0; i < vm.inboxTickets.length; ++i){
                        if(document.getElementById("inbox-checkbox-" + vm.inboxTickets[i].id).checked){
                            selectedInboxTickets.push(vm.inboxTickets[i].id.toString());
                        }
                    }
                    if(selectedInboxTickets.length != 0){
                        let check = confirm(this.s34);
                        if (check == true) {
                            axios({
                                method: "delete",
                                url: url + "/api/user/ticket", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedInboxTickets
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }else{
                        alert(this.s35);
                    }
                }else if(action == "close"){
                    selectedInboxTickets = [];
                    for(let i = 0; i < vm.inboxTickets.length; ++i){
                        if(document.getElementById("inbox-checkbox-" + vm.inboxTickets[i].id).checked){
                            selectedInboxTickets.push(vm.inboxTickets[i].id.toString());
                        }
                    }
                    if(selectedInboxTickets.length != 0){
                        let check = confirm(this.closeSelectedTicketsText);
                        if (check == true) {
                            axios({
                                method: "put",
                                url: url + "/api/supporter/ticket/status/2", //
                                headers: {"Content-Type": "application/json"},
                                data: JSON.stringify({
                                    names: selectedInboxTickets
                                }).replace(/\\\\/g, "\\")
                            }).then((res) => {
                                location.reload();
                            }).catch((error) => {
                                alert("ما قادر به پردازش درخواست شما نبودیم، لطفا دوباره امتحان کنید.");
                            });
                        }
                    }else{
                        alert(this.s35);
                    }
                }
            },
            rowSelected: function (id) {
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
            rowInboxSelected: function (id) {
                let row = document.getElementById("inbox-row-" + id);
                if(row.style.background == ""){
                    row.style.background = "#c2dbff";
                }else{
                    row.style.background = "";
                }
                this.allInboxIsSelected = false;
                if(document.getElementById("selectAllInboxCheckbox").checked == true){
                    document.getElementById("selectAllInboxCheckbox").click();
                }
            },
            deleteFilter: function () {
                this.idSearch = "";
                document.getElementById("dateSearch").value = "";
                this.getTickets();
            },
            deleteInboxFilter: function () {
                this.inboxIdSearch = "";
                document.getElementById("inboxDateSearch").value = "";
                this.getInboxTickets();
            },
            deleteArchivesFilter: function () {
                this.archivesIdSearch = "";
                document.getElementById("archivesDateSearch").value = "";
                this.getArchivesTickets();
            },
            removeIdSearch: function () {
                this.idSearch = "";
                this.getTickets();
            },
            removeDateSearch: function () {
                document.getElementById("dateSearch").value = "";
                this.getTickets();
            },
            removeInboxIdSearch: function () {
                this.inboxIdSearch = "";
                this.getInboxTickets();
            },
            removeInboxDateSearch: function () {
                document.getElementById("inboxDateSearch").value = "";
                this.getInboxTickets();
            },
            removeArchivesIdSearch: function () {
                this.archivesIdSearch = "";
                this.getArchivesTickets();
            },
            removeArchivesDateSearch: function () {
                document.getElementById("archivesDateSearch").value = "";
                this.getArchivesTickets();
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
                    this.deleteInputIcon = "right: 10%;";
                    this.s0 = "IOOC Centralized Authentication";
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
                    this.s25 = "Are You Sure You Want To Delete All Tickets?";
                    this.s27 = "Configs";
                    this.s29 = "Add Tickets";
                    this.s30 = "Edit Tickets";
                    this.s32 = "Delete Tickets";
                    this.s33 = "Action";
                    this.s34 = "Are You Sure You Want To Delete Selected Tickets?";
                    this.s35 = "No Ticket is Selected.";
                    this.s36 = "Records a Page: ";
                    this.s37 = "Audits";
                    this.s45 = "No Ticket Found";
                    this.rolesText = "Roles";
                    this.reportsText = "Reports";
                    this.publicmessagesText = "Public Messages";
                    this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingText = "Meeting";
                    this.enterMeetingText = "Enter Meeting";
                    this.inviteToMeetingText = "Invite To Meeting";
                    this.copyText = "Copy";
                    this.returnText = "Return";
                    this.ticketingText = "Ticketing";
                    this.transcriptsText = "Detailed Reports";
                    this.replyText = "Reply";
                    this.subjectText = "Subject";
                    this.fromText = "From";
                    this.sendNewMessageText = "Send New Message";
                    this.messageText = "Message";
                    this.newText = "New";
                    this.deleteText = "Delete";
                    this.sendText = "Send";
                    this.sendAndCloseText = "Send And Close Ticket";
                    this.openTicketsText = "Open Tickets";
                    this.myOpenTicketsText = "My Open Tickets";
                    this.allText = "All";
                    this.ticketsListText = "Tickets List";
                    this.closeText = "Close";
                    this.pendingTicketsText = "Pending Tickets";
                    this.closedTicketsText = "Closed Tickets";
                    this.createDateText = "Create Date";
                    this.idText = "ID";
                    this.submitFilter = "Filter";
                    this.removeFilter = "No Filter";
                    this.closeSelectedTicketsText = "Are You Sure You Want To Close Selected Tickets?";
                    this.statusText = "Status";
                    this.openText = "Open";
                    this.pendingText = "Pending";
                    this.closedText = "Closed";
                    this.alertMessage1Text = "On ";
                    this.alertMessage2Text = " User ";
                    this.alertMessageOpen3Text = ", Opened the Ticket.";
                    this.alertMessageClose3Text = ", Closed the Ticket.";
                    this.unreadMessageText ="Unanswered Message";
                    this.ticketArchivesText = "Ticket Archives";
                    this.detailText = "Detail";
                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;"
                    this.margin = "margin-right: 30px;";
                    this.lang = "EN";
                    this.isRtl = true;
                    this.margin1 = "ml-1";
                    this.dateNavText = this.dateNav;
                    this.deleteInputIcon = "left: 10%;";
                    this.s0 = "احراز هویت متمرکز شرکت فلات قاره";
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
                    this.s24 = "آیا از حذف این تیکت اطمینان دارید؟";
                    this.s25 = "آیا از حذف تمامی تیکت ها اطمینان دارید؟";
                    this.s26 = "./privacy";
                    this.s27 = "پیکربندی";
                    this.s29 = "افزودن تیکت";
                    this.s30 = "ویرایش تیکت";
                    this.s32 = "حذف تیکت";
                    this.s33 = "اعمال";
                    this.s34 = "آیا از حذف تیکت های انتخاب شده اطمینان دارید؟";
                    this.s35 = "هیچ تیکتی انتخاب نشده است.";
                    this.s36 = "تعداد رکورد ها: ";
                    this.s37 = "ممیزی ها";
                    this.s45 = "هیچ تیکتی یافت نشد";
                    this.rolesText = "نقش ها";
                    this.reportsText = "گزارش ها";
                    this.publicmessagesText = "اعلان های عمومی";
                    this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
                    this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
                    this.meetingText = "جلسه مجازی";
                    this.enterMeetingText = "ورود به جلسه";
                    this.inviteToMeetingText = "دعوت به جلسه";
                    this.copyText = "کپی";
                    this.returnText = "بازگشت";
                    this.ticketingText = "پشتیبانی";
                    this.transcriptsText = "گزارش های جزئی";
                    this.replyText = "پاسخ";
                    this.subjectText = "موضوع";
                    this.fromText = "ارسال کننده";
                    this.sendNewMessageText = "ارسال پیام جدید";
                    this.messageText = "پیام";
                    this.newText = "جدید";
                    this.deleteText = "حذف";
                    this.sendText = "ارسال";
                    this.sendAndCloseText = "ارسال و بستن تیکت";
                    this.openTicketsText = "تیکت های باز";
                    this.myOpenTicketsText = "تیکت های باز من";
                    this.allText = "همه";
                    this.ticketsListText = "لیست تیکت ها";
                    this.closeText = "بستن";
                    this.pendingTicketsText = "تیکت های در حال انتظار";
                    this.closedTicketsText = "تیکت های بسته شده";
                    this.createDateText = "تاریخ ایجاد";
                    this.idText = "شناسه";
                    this.submitFilter = "اعمال فیلتر";
                    this.removeFilter = "حذف فیلتر";
                    this.closeSelectedTicketsText = "آیا از بستن تیکت های انتخاب شده اطمینان دارید؟";
                    this.statusText = "وضعیت";
                    this.openText = "باز";
                    this.pendingText = "در حال انتظار";
                    this.closedText = "بسته شده";
                    this.alertMessage1Text = "تیکت در تاریخ ";
                    this.alertMessage2Text = " توسط کاربر ";
                    this.alertMessageOpen3Text = " باز شد.";
                    this.alertMessageClose3Text = " بسته شد.";
                    this.unreadMessageText = "پیام پاسخ داده نشده";
                    this.ticketArchivesText = "آرشیو تیکت ها";
                    this.detailText = "جزئیات";
                }
            }
        },
        computed:{
            sortedTickets: function () {
                this.ticketsPage = this.tickets;
                return this.ticketsPage;
            },
            sortedInboxTickets: function () {
                this.inboxTicketsPage = this.inboxTickets;
                return this.inboxTicketsPage;
            },
            sortedArchivesTickets: function () {
                this.archivesTicketsPage = this.archivesTickets;
                return this.archivesTicketsPage;
            },
        },
        watch : {
            paginationCurrentPage : function () {
                if(this.paginationCurrentPage != this.currentPage){
                    this.currentPage = this.paginationCurrentPage;
                    this.getTickets("paginationCurrentPage");
                }
            },
            paginationCurrentInboxPage : function () {
                if(this.paginationCurrentInboxPage != this.currentInboxPage){
                    this.currentInboxPage = this.paginationCurrentInboxPage;
                    if(userInfo.role == "SUPPORTER" || userInfo.role == "SUPERADMIN"){
                        this.getInboxTickets("paginationCurrentPage");
                    }else {
                        this.getSentTickets("paginationCurrentPage");
                    }
                }
            },
            paginationCurrentArchivesPage : function () {
                if(this.paginationCurrentArchivesPage != this.currentArchivesPage){
                    this.currentArchivesPage = this.paginationCurrentArchivesPage;
                    if(userInfo.role == "SUPERADMIN"){
                        this.getArchivesTickets("paginationCurrentPage");
                    }
                }
            },
        }
    });
})
