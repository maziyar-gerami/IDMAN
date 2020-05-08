let html = "<div class='modal fade' id='userSelectorModal' tabindex='-1' role='dialog'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><h4 class='modal-title'>جستجوی کاربر</h4><button type='button' class='close modal-white-close' data-dismiss='modal'><span aria-hidden='true'>&times;</span></button></div><div class='modal-body'>\n" +
    "<user-selector-search api='/client/users/search' @selected='selected'></user-selector-search>" +
    "</div><div class='modal-footer bg-default'><button id='userSelectorConfirm' type='button' class='btn btn-primary' disabled>تایید</button></div></div></div></div>";

const del = () => {
    $('#userSelectorModal').remove();
};

module.exports = function () {
    return new Promise(((resolve, reject) => {
        let finished = false, selected = null;
        del();

        document.body.appendChild($(html)[0]);

        let vm = new Vue({
            el: '#userSelectorModal',
            methods: {
                selected(user) {
                    selected = user;
                    $('#userSelectorConfirm').prop('disabled', false)
                }
            }
        });

        $('#userSelectorConfirm').click(x => {
            finished = true;
            $('#userSelectorModal').modal('hide');
        });

        $('#userSelectorModal').on('hidden.bs.modal', x => {
            vm.$destroy();
            del();

            if (finished) {
                resolve(selected);
            } else {
                reject();
            }
        }).modal('show');
    }))
};