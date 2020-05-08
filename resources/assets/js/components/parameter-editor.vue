<template>
    <div>
        <div class="form-group" v-for="parameter in parameters">
            <label :for="`param_${parameter.name}`" v-text="parameter.title"></label>
            <input :id="`param_${parameter.name}`" type="text" class="form-control" v-model="value[parameter.id]"
                   :disabled="parameter.attached_to != null || parameter.user_inputtable">
        </div>
    </div>
</template>
<script>
    export default {
        props: {
            parameters: {
                required: true,
                type: Array,
            },
            value: {
                type: Object,
                default: () => {
                    return {}
                },
            },
        }
    }
</script>