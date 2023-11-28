import {
  // create naive ui
  create,
  // component
  NButton,
  NCheckbox,
  NCheckboxGroup,
  NDataTable,
  NDropdown,
  NForm,
  NFormItem,
  NIcon,
  NInput,
  NSelect,
  NSkeleton,
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
    NCheckboxGroup,
    NSkeleton,
  ],
})

export default naive
