import {
  // create naive ui
  create,
  // component
  NButton,
  NCheckbox,
  NDataTable,
  NDropdown,
  NForm,
  NFormItem,
  NIcon,
  NInput,
  NSelect,
  NSpace,
  NSwitch,
  NTooltip,
} from "naive-ui"

const naive = create({
  components: [
    NButton,
    NSelect,
    NInput,
    NSwitch,
    NForm,
    NFormItem,
    NTooltip,
    NDropdown,
    NIcon,
    NDataTable,
    NCheckbox,
    NSpace,
  ],
})

export default naive
