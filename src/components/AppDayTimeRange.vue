<template>
  <div class="formgrid grid">
    <div class="field col-4">
      <div class="field p-fluid">
        <Checkbox :id="scope + 'SelectAll'" v-model="selectAll" @input="selectAllHelper()" :binary="true" />
        <label :for="scope + 'SelectAll'" class="mx-2 mb-0">{{ $t("selectAll") }}</label>
      </div>
    </div>
  </div>
  <div v-for="i in [...Array(7).keys()]" v-bind:key="i" class="formgrid grid">
    <div class="field col-4">
      <div class="mx-3">
        <Checkbox :id="scope + 'Select' + i" v-model="checkbox[i]" :binary="true" @input="check(i)" />
        <label :for="scope + 'Select' + i" class="mx-2 mb-0">{{ $t("daysArray[" + i + "]") }}</label>
      </div>
    </div>
    <div class="field col-8">
      <div class="flex" :style="checkbox[i] ? '' : 'visibility: hidden;'">
        <label class="mx-2">{{ $t("fromTime") }}</label>
        <InputText :id="startDateId + i" type="text" value="۰:۰۰" class="timePickerFa mx-1" />
        <label class="mx-2">{{ $t("toTime") }}</label>
        <InputText :id="endDateId + i" type="text" value="۲۳:۵۹" class="timePickerFa mx-1" />
      </div>
    </div>
  </div>
</template>

<script>
import "persian-datepicker/dist/js/persian-datepicker"

export default {
  name: "AppDayTimeRange",
  props: {
    startDateId: String,
    endDateId: String,
    scope: String
  },
  data () {
    return {
      selectAll: false,
      checkbox: [false, false, false, false, false, false, false]
    }
  },
  mounted () {
    // eslint-disable-next-line no-undef
    jQuery(".datePickerFa").pDatepicker({
      inline: false,
      format: "LLLL",
      viewMode: "day",
      initialValue: false,
      initialValueType: "persian",
      autoClose: false,
      position: "auto",
      altFormat: "lll",
      altField: ".alt-field",
      onlyTimePicker: false,
      onlySelectOnDate: false,
      calendarType: "persian",
      inputDelay: 800,
      observer: false,
      calendar: {
        persian: {
          locale: "fa",
          showHint: true,
          leapYearMode: "algorithmic"
        },
        gregorian: {
          locale: "en",
          showHint: true
        }
      },
      navigator: {
        enabled: true,
        scroll: {
          enabled: true
        },
        text: {
          btnNextText: "<",
          btnPrevText: ">"
        }
      },
      toolbox: {
        enabled: true,
        calendarSwitch: {
          enabled: true,
          format: "MMMM"
        },
        todayButton: {
          enabled: true,
          text: {
            fa: "امروز",
            en: "Today"
          }
        },
        submitButton: {
          enabled: false,
          text: {
            fa: "تایید",
            en: "Submit"
          }
        },
        text: {
          btnToday: "امروز"
        }
      },
      timePicker: {
        enabled: true,
        step: 1,
        hour: {
          enabled: true,
          step: null
        },
        minute: {
          enabled: true,
          step: null
        },
        second: {
          enabled: false,
          step: null
        },
        meridian: {
          enabled: true
        }
      },
      dayPicker: {
        enabled: true,
        titleFormat: "YYYY MMMM"
      },
      monthPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      yearPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      responsive: true
    })
    // eslint-disable-next-line no-undef
    jQuery(".timePickerFa").pDatepicker({
      inline: false,
      format: "H:mm",
      viewMode: "day",
      initialValue: false,
      autoClose: false,
      position: "auto",
      altFormat: "H:mm",
      altField: "#altfieldExample",
      onlyTimePicker: true,
      onlySelectOnDate: false,
      calendarType: "persian",
      inputDelay: 800,
      observer: false,
      calendar: {
        persian: {
          locale: "fa",
          showHint: false,
          leapYearMode: "algorithmic"
        },
        gregorian: {
          locale: "en",
          showHint: false
        }
      },
      navigator: {
        enabled: true,
        scroll: {
          enabled: true
        },
        text: {
          btnNextText: "<",
          btnPrevText: ">"
        }
      },
      toolbox: {
        enabled: false,
        calendarSwitch: {
          enabled: false,
          format: "MMMM"
        },
        todayButton: {
          enabled: false,
          text: {
            fa: "امروز",
            en: "Today"
          }
        },
        submitButton: {
          enabled: false,
          text: {
            fa: "تایید",
            en: "Submit"
          }
        },
        text: {
          btnToday: "امروز"
        }
      },
      timePicker: {
        enabled: true,
        step: 1,
        hour: {
          enabled: true,
          step: null
        },
        minute: {
          enabled: true,
          step: null
        },
        second: {
          enabled: false,
          step: null
        },
        meridian: {
          enabled: false
        }
      },
      dayPicker: {
        enabled: false,
        titleFormat: "YYYY MMMM"
      },
      monthPicker: {
        enabled: false,
        titleFormat: "YYYY"
      },
      yearPicker: {
        enabled: false,
        titleFormat: "YYYY"
      },
      responsive: true
    })
  },
  methods: {
    selectAllHelper () {
      this.checkbox = [this.selectAll, this.selectAll, this.selectAll, this.selectAll, this.selectAll, this.selectAll, this.selectAll]
    },
    check (i) {
      if (this.checkbox.every(element => element === true)) {
        this.selectAll = true
      } else if (this.checkbox[i] === false) {
        this.selectAll = false
      }
    }
  }
}
</script>
