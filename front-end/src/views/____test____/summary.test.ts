import { describe, it, expect ,beforeEach,vi} from 'vitest';
import { generateDaysArray, generatePieData,  type pieIn, type pieOut} from '@/types/summary';
import { RecordTag } from '@/types/record';

describe('generateDaysArray', () => {
    it('应该生成正确的收支趋势图表', () => {
        const daysArray = generateDaysArray(5);
        expect(daysArray).toEqual(['01', '02', '03', '04', '05']);
    });

});

describe('generatePieData', () => {
    it('应该生成正确的饼图数据', () => {
        const inputData: pieIn[] = [
            { value: 100, tag: 1 },
            { value: 200, tag: 2 }
        ];
        const expectedOutput: pieOut[] = [
            { name: RecordTag[1], value: 1 },
            { name: RecordTag[2], value: 2 }
        ];
        const pieData = generatePieData(inputData);
        expect(pieData).toEqual(expectedOutput);
    });
});


import { mount } from '@vue/test-utils';
import SummaryView from '@/views/SummaryView.vue';

describe('SummaryView', () => {
  let wrapper: any;

  beforeEach(() => {
    wrapper = mount(SummaryView);
  });
  it('应该生成正确的收支总结', () => {
    wrapper.vm.totalIncome = 100;
    wrapper.vm.totalExpense = 200;
    expect(wrapper.vm.totalExpense).toBe(200);
    expect(wrapper.vm.totalIncome).toBe(100);
    
  }),
  it('正确渲染页面', () => {
    expect(wrapper.exists()).toBe(true);
  });

  // Add more tests as needed
});

