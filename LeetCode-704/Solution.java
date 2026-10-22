// ============================================================================
// 力扣 704. 二分查找
// ----------------------------------------------------------------------------
// 题目描述
// 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target，写一个
// 函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
// 你必须编写一个具有 O(log n) 时间复杂度的算法。
//
// 函数签名（力扣已经给出，不用自己读输入，把方法补全提交即可）
//   public int search(int[] nums, int target)
//
// 输入 #1
//   nums = [-1,0,3,5,9,12], target = 9
// 输出 #1
//   4
//   解释：9 出现在 nums 中并且下标为 4。
//
// 输入 #2
//   nums = [-1,0,3,5,9,12], target = 2
// 输出 #2
//   -1
//   解释：2 不存在 nums 中因此返回 -1。
//
// 提示
//   1. 你可以假设 nums 中的所有元素是不重复的。
//   2. n 将在 [1, 10000] 之间。
//   3. nums 的每个元素都将在 [-9999, 9999] 之间。
//
// 说明/提示
// 【样例说明】两个样例都拿二分实际走一遍：
//   例 1：nums = [-1,0,3,5,9,12]，target = 9
//     left=0, right=5 → mid=2，nums[2]=3 < 9 → left=3
//     left=3, right=5 → mid=4，nums[4]=9 == 9 → 返回 4，与样例一致。
//   例 2：nums = [-1,0,3,5,9,12]，target = 2
//     left=0, right=5 → mid=2，nums[2]=3 > 2 → right=1
//     left=0, right=1 → mid=0，nums[0]=-1 < 2 → left=1
//     left=1, right=1 → mid=1，nums[1]=0 < 2 → left=2
//     left=2 > right=1，区间空了，退出循环返回 -1，与样例一致。
// 【评测用例规模与约定】
//   n 在 [1, 10000]，nums 升序且元素不重复，元素在 [-9999, 9999]；
//   题面要求 O(log n)，二分查找正好满足。力扣难度：简单。
//
// 思路（闭区间二分：left / right 两个下标 + while 循环，全是基础写法）
//   二分查找的前提是数组有序（题面已经保证升序）。核心想法：每次盯住当前
//   查找范围的"中间位置" mid，拿 nums[mid] 和 target 比一次，就能一口气
//   排除掉一半的元素：
//     - nums[mid] == target：找到了，直接返回 mid；
//     - nums[mid] <  target：target 只可能在 mid 右边，让 left = mid + 1；
//     - nums[mid] >  target：target 只可能在 mid 左边，让 right = mid - 1。
//   每比一次范围就缩小一半，最多比 log2(n) 次，所以是 O(log n)。
//
//   具体写法（闭区间，最容易记、最不容易错的一种）：
//     ① 初始化 left = 0、right = nums.length - 1，表示当前在 [left, right]
//        这个闭区间里找，两端的元素都还没被排除；
//     ② 循环条件写 while (left <= right)——区间里还有元素就继续；当
//        left > right 时区间空了，说明找遍了也没有 target，退出后返回 -1；
//     ③ mid 取中点：mid = left + (right - left) / 2。本题下标最多到 10000，
//        写成 (left + right) / 2 也不会溢出，但前一种是通用好习惯，以后
//        数组很长时 left + right 可能超出 int；
//     ④ 循环里按上面三条规则移动指针，注意是 mid + 1 / mid - 1 而不是
//        mid——mid 位置刚刚比过了，不能留在下一轮的区间里。
//
//   边界要留意的点：
//     - 循环条件 <= 和 right = nums.length - 1 是配套的（闭区间）。另一种
//       常见写法是左闭右开 [left, right)：right 初始化为 nums.length、
//       循环条件写 left < right、right 更新成 mid。两种写法都对，但千万别
//       混着写，混了容易死循环或漏掉最后一个元素；
//     - n 至少是 1（提示里 n 在 [1, 10000]），不用特判空数组；不过写完可以
//       自己加一组"数组只有一个元素"来验证：nums = [5]、target = 5 应返回
//       0；target = 4 应返回 -1；
//     - 题面保证元素不重复，所以找到就可以直接返回，不用再向左右找重复的；
//       如果有重复且要求第一个/最后一个位置，才需要额外处理；
//     - 时间复杂度 O(log n)，空间复杂度 O(1)（只用了几个下标变量）。
// ============================================================================

public class Solution {
    public int search(int[] nums, int target){
        int left = 0;
        int right = nums.length - 1;
        while (left <= right){
            int mid = (left + right) / 2;
            if(nums[mid] > target){
                right = mid - 1;
            }else if(nums[mid] < target){
                left = mid + 1;
            }else{
                return mid;
            }
        }
        return -1;
    }
}
