<template>
  <div class="bootstrap">
    <div class="card p-2">
      <div class="card-header bg-light p-2 d-flex justify-content-between align-items-center">
        <h5>{{ label }}</h5>
        <div class="d-flex" style="overflow: auto;">
          <div style="width: 250px;">
            <div class="input-group input-group-sm">
              <InputText type="text" class="mx-2" v-model="search" :placeholder="$t('search')" />
            </div>
          </div>
        </div>
      </div>
      <div class="list-group list-group-flush" style="max-height: 300px !important; overflow: auto;">
        <div v-if="loader" class="text-center">
          <ProgressSpinner />
        </div>
        <div v-else>
          <div class="list-group-item" v-if="result.length > 0">
            <div class="row align-items-start">
              <div class="card m-2 sm:col-4 md:col-3 lg:col-2 text-center" v-for="record in result" v-bind:key="record[firstParameter]">
                <div>
                  <label>{{ record[firstParameter] }}</label>
                  <div class="text-muted text-ellipsis mb-2">
                    <small>{{ record[secondParameter] }}</small>
                  </div>
                  <div class="row">
                    <div v-if="parentList" class="col-6">
                      <Button @click="$emit('toggleRecord', record, type, 'add')" icon="bx bx-plus bx-sm" v-tooltip.top="$t('grantAccess')" class="p-button-rounded p-button-success p-button-outlined p-button-sm" />
                    </div>
                    <div v-if="parentList" class="col-6">
                      <Button @click="$emit('toggleRecord', record, type, 'remove')" icon="bx bx-minus-circle bx-sm" v-tooltip.top="$t('revokeAccess')" class="p-button-rounded p-button-danger p-button-outlined p-button-sm" />
                    </div>
                    <div v-if="!parentList" class="col-12">
                      <Button @click="$emit('toggleRecord', record, type, '')" icon="bx bx-x bx-sm" v-tooltip.top="$t('delete')" class="p-button-rounded p-button-danger p-button-outlined p-button-sm" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-if="result.length === 0 && search.length !== 0" class="list-group-item text-center">
            <div class="text-center" style="display: grid;">
              <i class="bx bx-info-circle bx-lg"></i>
              {{ $t("noRecordsFound") }}
            </div>
          </div>
          <div v-if="result.length === 0 && search.length === 0" class="list-group-item list-group-item-warning text-center">
            <div class="text-center" style="display: grid;">
              <i class="bx bx-info-circle bx-lg"></i>
              {{ $t("listIsEmpty") }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AppListBox",
  props: {
    label: String,
    list: Array,
    searchParameters: Array,
    firstParameter: String,
    secondParameter: String,
    parentList: Boolean,
    loader: Boolean,
    type: String
  },
  data () {
    return {
      search: ""
    }
  },
  computed: {
    result () {
      if (this.search !== "") {
        let buffer = []
        this.searchParameters.forEach(parameter => {
          buffer = buffer.concat(this.list.filter((item) => {
            return this.search.toLowerCase().split(" ").every(v => item[parameter].toLowerCase().includes(v))
          }))
        })
        const uniqueBuffer = [...new Set(buffer)]
        return uniqueBuffer
      } else {
        return this.list
      }
    }
  }
}
</script>
