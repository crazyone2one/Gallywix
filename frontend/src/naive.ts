import {
  // create naive ui
  create,
  // component
  NButton,
  NInput,
  NSelect,
  NSwitch,
} from "naive-ui"

const naive = create({
  components: [NButton, NSelect, NInput, NSwitch],
})

export default naive
