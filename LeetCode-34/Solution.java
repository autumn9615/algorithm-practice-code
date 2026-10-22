// ============================================================================
// 力扣 34. 在排序数组中查找元素的第一个和最后一个位置
// ----------------------------------------------------------------------------
// 题目描述
// 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出
// 给定目标值在数组中的开始位置和结束位置。
// 如果数组中不存在目标值 target，返回 [-1, -1]。
// 你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
//
// 函数签名（力扣已经给出，不用自己读输入，把方法补全提交即可）
//   public int[] searchRange(int[] nums, int target)
//
// 输入 #1
//   nums = [5,7,7,8,8,10], target = 8
// 输出 #1
//   [3,4]
//
// 输入 #2
//   nums = [5,7,7,8,8,10], target = 6
// 输出 #2
//   [-1,-1]
//
// 输入 #3
//   nums = [], target = 0
// 输出 #3
//   [-1,-1]
//
// 提示
//   1. 0 <= nums.length <= 10^5
//   2. -10^9 <= nums[i] <= 10^9
//   3. nums 是一个非递减数组
//   4. -10^9 <= target <= 10^9
//
// 说明/提示
// 【样例说明】样例 1 里 8 出现了两次，下标 3 和 4，所以返回 [3,4]；
//   样例 2 里根本没有 6，返回 [-1,-1]；样例 3 是空数组，也返回 [-1,-1]。
//   注意"非递减"意味着可以有重复元素，这正是和上一题 704（元素不重复）的
//   关键区别：704 用普通二分找到某一个就返回，这道题要找到一组相同值的
//   "最左端"和"最右端"，所以同一个二分思路要用两遍。
// 【评测用例规模与约定】
//   n 最多 10^5，元素最大到 10^9（int 放得下），题面要求 O(log n)，
//   所以不能线性扫描找左右端点，必须用二分。力扣难度：中等。
//
// 思路（两次"找边界"的二分：左闭右开 + while (left < right)，在 704 的基础上演变）
//   先想清楚要二分找的是什么：
//     - 第一个位置 = 第一个满足 nums[i] >= target 的下标（"找下界"）；
//     - 最后一个位置 = 最后一个满足 nums[i] <= target 的下标，也就是
//       第一个满足 nums[i] > target 的下标再减 1（"找上界"的邻居）。
//   704 的闭区间二分只回答"有没有、在哪个"；这里要回答"第一个在哪"，
//   所以找到 nums[mid] == target 时不能直接返回，得继续往左收缩，
//   逼着区间缩到最左端为止。
//
//   统一写成左闭右开区间 [left, right) 的二分，写一个通用的 lowerBound：
//     // 返回第一个满足 nums[i] >= target 的下标；不存在则返回 nums.length
//     int lowerBound(int[] nums, int target) {
//         int left = 0, right = nums.length;   // 循环不变量：答案在 [left, right] 里
//         while (left < right) {
//             int mid = left + (right - left) / 2;   // 下标最多 10^5，防溢出好习惯
//             if (nums[mid] < target) {
//                 left = mid + 1;          // mid 及左边都不可能是答案
//             } else {
//                 right = mid;             // mid 可能是答案，保留在区间里
//             }
//         }
//         return left;                     // left == right 时就是答案（也可能 = n）
//     }
//
//   有了它，两个端点就都能算出来：
//     - 第一个位置 start = lowerBound(nums, target)；
//     - 最后一个位置 end：先求 p = lowerBound(nums, (int) ((long) target + 1))，
//       它是第一个严格大于 target 的位置，那 end = p - 1。注意 target 最大
//       就是 int 最大值时 target + 1 会溢出，所以先转 long 再加 1 再转回来；
//       本题 target 最大 10^9 其实不会溢出，但养成这个习惯不会错。
//
//   最后判断：如果 start == nums.length（没找到比 target 大的位置，即全部
//   小于 target）或者 nums[start] != target（下界处的值不是 target），说明
//   数组里没有 target，返回 [-1, -1]；否则返回 [start, end]。
//
//   用样例 1 走一遍（nums = [5,7,7,8,8,10], target = 8）：
//     lowerBound(8)：left=0,right=6 → mid=3 nums[3]=8 不小于 8 → right=3
//                    left=0,right=3 → mid=1 nums[1]=7 < 8 → left=2
//                    left=2,right=3 → mid=2 nums[2]=7 < 8 → left=3
//                    left=3=right → 返回 3。start = 3，nums[3] == 8 ✓
//     lowerBound(9)：left=0,right=6 → mid=3 nums[3]=8 < 9 → left=4
//                    left=4,right=6 → mid=5 nums[5]=10 不小于 9 → right=5
//                    left=4,right=5 → mid=4 nums[4]=8 < 9 → left=5
//                    left=5=right → 返回 5。end = 5-1 = 4。返回 [3,4] ✓
//
//   边界要留意的点：
//     - 空数组：lowerBound 直接返回 0，start == nums.length 走"没找到"
//       分支返回 [-1,-1]，不用特判；
//     - 左闭右开和 704 的闭区间写法不要混用：这里 right 初始化成
//       nums.length（不是 length-1）、条件写 left < right、right 更新成 mid
//       （不是 mid-1），三者是配套的。混写会死循环或漏掉最后一个元素；
//     - 找到相等值时千万别直接 return，要让它作为 right = mid 收缩下去，
//       这才是"找第一个"的关键；
//     - lowerBound 返回值可能是 nums.length，用之前必须先判断，不能直接
//       拿去 nums[x] 访问；
//     - 时间复杂度 O(log n)（两遍二分），空间复杂度 O(1)。
// ============================================================================

class Solution {
    public int[] searchRange(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        int[] ans = new int[2];
        while (start <= end) {
            int mid = start + (end - start) / 2;
            
        }
    }
}