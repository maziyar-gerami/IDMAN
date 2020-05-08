<template>
    <div class="form-group" ref="inputWrapper" :class="{ 'active': active }">

        <label class="control-label" :for="name + '-input'" v-if="label" ref="inputLabel">
            <span>{{ label }}{{ required ? '*' : '' }}</span>
        </label>

        <div class="input-group" :class="[ color ? 'input-group-' + color : '', size ? 'input-group-' + size : '']">
            <span v-if="icon" class="input-group-addon">
                <icon :icon="icon"></icon>
            </span>

            <input :id="name + '-input'" type="text" :name="name" v-model="submitValue" class="form-control"
                   :placeholder="placeholder ? placeholder : ''" :step="step"
                   :aria-label="placeholder ? placeholder : ''"
                   :class="{'is-invalid': errorMessage || !valid && contentChanged, 'is-valid': valid && contentChanged}"
                   :disabled="disabled" ref="input" @focus="activate" @blur="deactivate">

            <div class="input-group-append has-tooltip" v-if="help" ref="helpIcon">
                <span class="input-group-text" :title="help" v-tooltip:left>
                    <icon icon="fa fa-question"></icon>
                </span>
            </div>

            <span class="input-group-btn" v-if="showAddonSubmit && submitValue">
                <button class="btn" :class="addonResetColor ? 'btn-' + addonResetColor : ''" type="button"
                        v-on:click="clearSubmit"><icon icon="fa fa-fw fa-times"></icon></button>
            </span>

            <span class="input-group-btn" v-if="showAddonSubmit">
                <slot name="appendbutton"></slot>
                <button v-if="!$slots.appendbutton" class="btn"
                        :class="addonSubmitColor ? 'btn-' + addonSubmitColor : ''"
                        type="submit">{{ addonSubmitContent }}</button>
            </span>
        </div>

        <div class="invalid-feedback d-block" v-if="errorMessage">{{ errorMessage }}</div>

        <span v-if="showMaxLengthCounter" class="counter">{{ submitValue ? submitValue.length : 0 }} / {{ max }}</span>
        <span v-if="showMinLengthCounter" class="counter">{{ submitValue ? submitValue.length : 0 }} / {{ min }}</span>
    </div>
</template>

<script>
    import formInputMixin from '../../mixins/TextFormInputMixin';

    export default {
        mixins: [formInputMixin],

        props: {

            // The type of the input field.
            type: {
                type: String,
                default: 'text'
            },

            // The step to increase the value if the input's type is set to "number".
            step: {
                type: String,
                default: ''
            }
        },

        mounted() {
            this.$nextTick(function () {

                // Necessary, because of setting the type directly is not possible with vue.
                $(this.$refs.input).attr('type', this.type);
            })
        }
    }
</script>
