let html = "<div class='modal fade' id='parameterEditorModal' tabindex='-1' role='dialog'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><h4 class='modal-title'>ویرایش پارامتر ها</h4><button type='button' class='close modal-white-close' data-dismiss='modal'><span aria-hidden='true'>&times;</span></button></div><div class='modal-body'>\n" +
    "<parameter-editor :parameters='parameters' v-model='values'></parameter-editor>" +
    "</div><div class='modal-footer bg-default'><button id='parameterEditorConfirm' type='button' class='btn btn-primary'>تایید</button></div></div></div></div>";

const del = () => {
    $('#parameterEditorModal').remove();
};

module.exports = function (parameters, values) {
    return new Promise(((resolve, reject) => {
        del();

        document.body.appendChild($(html)[0]);

        let vm = new Vue({
            el: '#parameterEditorModal',
            data() {
                return {
                    parameters: parameters,
                    values: values,
                }
            },
        });

        $('#parameterEditorConfirm').click(x => {
            $('#parameterEditorModal').modal('hide');
        });

        $('#parameterEditorModal').on('hidden.bs.modal', x => {
            let data = vm.$data.values;
            vm.$destroy();
            del();
            resolve(data);
        }).modal('show');
    }))
};