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
  NPopover,
  NSelect,
  NSkeleton,
  NSpace,
  NSpin,
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
    NSpin,
    NPopover,
  ],
})

export default naive
